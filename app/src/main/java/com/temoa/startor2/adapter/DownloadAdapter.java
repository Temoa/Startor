package com.temoa.startor2.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadListener;
import com.liulishuo.filedownloader.FileDownloadSampleListener;
import com.liulishuo.filedownloader.FileDownloader;
import com.liulishuo.filedownloader.model.FileDownloadStatus;
import com.liulishuo.filedownloader.util.FileDownloadUtils;
import com.temoa.startor2.R;
import com.temoa.startor2.utils.TaskManager;
import com.temoa.startor2.beans.Task;

import java.io.File;

/**
 * Created by Temoa
 * on 2016/10/24 19:41
 */

public class DownloadAdapter extends RecyclerView.Adapter<DownloadAdapter.TaskItemHolder> {

    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private PlayClickListener mPlayClickListener;

    private FileDownloadListener mDownloadListener = new FileDownloadSampleListener() {

        private TaskItemHolder checkCurrentHolder(BaseDownloadTask task) {
            TaskItemHolder tag = (TaskItemHolder) task.getTag();
            if (tag.downloadId != task.getId())
                return null;

            return tag;
        }

        @Override
        protected void pending(BaseDownloadTask task, int soFarBytes, int totalBytes) {
            super.pending(task, soFarBytes, totalBytes);
            TaskItemHolder tag = checkCurrentHolder(task);
            if (tag == null)
                return;
            tag.updateDownloading(FileDownloadStatus.pending, soFarBytes, totalBytes);
        }

        @Override
        protected void started(BaseDownloadTask task) {
            super.started(task);
            TaskItemHolder tag = checkCurrentHolder(task);
            if (tag == null)
                return;
            tag.updateDownloading(FileDownloadStatus.started, 0, 0);
            notifyDataSetChanged();
        }

        @Override
        protected void connected(BaseDownloadTask task, String etag, boolean isContinue, int soFarBytes, int totalBytes) {
            super.connected(task, etag, isContinue, soFarBytes, totalBytes);
            TaskItemHolder tag = checkCurrentHolder(task);
            if (tag == null)
                return;
            tag.updateDownloading(FileDownloadStatus.connected, soFarBytes, totalBytes);
        }

        @Override
        protected void progress(BaseDownloadTask task, int soFarBytes, int totalBytes) {
            super.progress(task, soFarBytes, totalBytes);
            TaskItemHolder tag = checkCurrentHolder(task);
            if (tag == null)
                return;
            tag.updateDownloading(FileDownloadStatus.progress, soFarBytes, totalBytes);
        }

        @Override
        protected void completed(BaseDownloadTask task) {
            super.completed(task);
            TaskItemHolder tag = checkCurrentHolder(task);
            if (tag == null)
                return;
            tag.updateDownloaded();
            TaskManager.getImpl().removeTaskForViewHolder(task.getId());
        }

        @Override
        protected void paused(BaseDownloadTask task, int soFarBytes, int totalBytes) {
            super.paused(task, soFarBytes, totalBytes);
            TaskItemHolder tag = checkCurrentHolder(task);
            if (tag == null)
                return;
            tag.updateNotDownloaded(FileDownloadStatus.paused, soFarBytes, totalBytes);
        }

        @Override
        protected void error(BaseDownloadTask task, Throwable e) {
            super.error(task, e);
            TaskItemHolder tag = checkCurrentHolder(task);
            if (tag == null)
                return;
            tag.updateNotDownloaded(FileDownloadStatus.error,
                    task.getLargeFileSoFarBytes(),
                    task.getLargeFileTotalBytes());
            e.printStackTrace();
        }
    };

    private View.OnClickListener mPlayBtnOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v.getTag() == null)
                return;

            TaskItemHolder holder = (TaskItemHolder) v.getTag();
            CharSequence action = holder.taskStatusTv.getText();

            if (action.equals(mContext.getString(R.string.download_adpater_pause))) {
                startTask(holder);
            } else if (action.equals(mContext.getString(R.string.download_adapter_loaded))) {
                Task task = TaskManager.getImpl().get(holder.position);
                if (mPlayClickListener != null)
                    mPlayClickListener.onPlayClickListener(v, task);
            } else {
                pauseTask(holder);
            }
        }
    };

    private View.OnClickListener mDeleteBtnOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(final View v) {
            if (mContext == null)
                return;

            if (v.getTag() == null)
                return;


            AlertDialog.Builder builder = new AlertDialog.Builder(mContext)
                    .setTitle(mContext.getString(R.string.remind))
                    .setMessage(R.string.download_adapter_if_delete)
                    .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            TaskItemHolder holder = (TaskItemHolder) v.getTag();
                            Task task = TaskManager.getImpl().get(holder.position);
                            TaskManager.getImpl().removeTaskForViewHolder(holder.downloadId);
                            TaskManager.getImpl().removeTask(task);
                            if (!holder.taskNameTv.getText().equals(mContext.getString(R.string.download_adapter_loaded)))
                                FileDownloader.getImpl().clear(task.getId(), task.getPath());

                            File videoFile = new File(task.getPath());
                            if (videoFile.exists()) {
                                videoFile.delete();
                            }

                            notifyDataSetChanged();
                        }
                    })
                    .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            builder.create().show();
        }
    };

    public interface PlayClickListener {
        void onPlayClickListener(View v, Task task);
    }

    public DownloadAdapter(Context context) {
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public TaskItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mLayoutInflater.inflate(R.layout.download_recycler_item, parent, false);
        return new TaskItemHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final TaskItemHolder holder, int position) {
        Task task = TaskManager.getImpl().get(position);

        holder.update(task.getId(), position);
        holder.taskNameTv.setText(task.getName());
        holder.taskPlayBtn.setTag(holder);
        holder.taskPlayBtn.setOnClickListener(mPlayBtnOnClickListener);
        holder.taskDeleteBtn.setTag(holder);
        holder.taskDeleteBtn.setOnClickListener(mDeleteBtnOnClickListener);

        startTask(holder);

        if (TaskManager.getImpl().isReady()) {
            holder.taskPlayBtn.setClickable(true);
            holder.taskDeleteBtn.setClickable(true);

            int status = TaskManager.getImpl().getStatus(task.getId(), task.getPath());

            if (status == FileDownloadStatus.pending
                    || status == FileDownloadStatus.started
                    || status == FileDownloadStatus.connected) {
                // 开始任务,当时文件还有没创建
                holder.updateDownloading(status,
                        TaskManager.getImpl().getSoFarBytes(task.getId()),
                        TaskManager.getImpl().getTotalBytes(task.getId()));
            } else if (!new File(task.getPath()).exists()
                    && !new File(FileDownloadUtils.getTempPath(task.getPath())).exists()) {
                // 文件不存在
                holder.updateNotDownloaded(status, 0, 0);
            } else if (TaskManager.getImpl().isDownload(status)) {
                // 已经下载完成,并且文件存在
                holder.updateDownloaded();
            } else if (status == FileDownloadStatus.progress) {
                // 正在下载
                holder.updateDownloading(status,
                        TaskManager.getImpl().getSoFarBytes(task.getId()),
                        TaskManager.getImpl().getTotalBytes(task.getId()));
            } else {
                // 还没开始下载
                holder.updateNotDownloaded(status,
                        TaskManager.getImpl().getSoFarBytes(task.getId()),
                        TaskManager.getImpl().getTotalBytes(task.getId()));
            }
        } else {
            holder.taskPlayBtn.setClickable(false);
            holder.taskDeleteBtn.setClickable(false);
        }
    }

    @Override
    public int getItemCount() {
        return TaskManager.getImpl().getTaskCount();
    }

    private void startTask(TaskItemHolder holder) {
        Task model = TaskManager.getImpl().get(holder.position);
        BaseDownloadTask baseDownloadTask = FileDownloader.getImpl().create(model.getUrl())
                .setPath(model.getPath())
                .setCallbackProgressTimes(100)
                .setListener(mDownloadListener);

        TaskManager.getImpl().addTaskForViewHolder(baseDownloadTask);

        TaskManager.getImpl().updateTaskForViewHolder(holder.downloadId, holder);

        baseDownloadTask.start();
    }

    private void pauseTask(TaskItemHolder holder) {
        FileDownloader.getImpl().pause(holder.downloadId);
        holder.taskPlayBtn.setImageResource(R.drawable.ic_video_download_play_24dp);
    }

    public void setPlayClickListener(PlayClickListener listener) {
        mPlayClickListener = listener;
    }

    public class TaskItemHolder extends RecyclerView.ViewHolder {
        private TextView taskNameTv;
        private TextView taskStatusTv;
        private ProgressBar taskPb;
        private ImageButton taskPlayBtn, taskDeleteBtn;

        private int position;
        private int downloadId;

        public TaskItemHolder(View itemView) {
            super(itemView);
            assignViews();
        }

        private void assignViews() {
            taskNameTv = (TextView) itemView.findViewById(R.id.item_title);
            taskStatusTv = (TextView) itemView.findViewById(R.id.item_status);
            taskPb = (ProgressBar) itemView.findViewById(R.id.item_progress_bar);
            taskPlayBtn = (ImageButton) itemView.findViewById(R.id.item_play);
            taskDeleteBtn = (ImageButton) itemView.findViewById(R.id.item_delete);
        }


        public void update(int downloadId, int position) {
            this.downloadId = downloadId;
            this.position = position;
        }

        public void updateDownloaded() {
            taskPb.setVisibility(View.GONE);

            taskStatusTv.setText(mContext.getString(R.string.download_adapter_loaded));
            taskPlayBtn.setImageResource(R.drawable.ic_video_download_play_24dp);
        }

        public void updateNotDownloaded(int status, long soFar, long total) {
            if (soFar > 0 && total > 0) {
                float percent = soFar / (float) total;
                taskPb.setVisibility(View.VISIBLE);
                taskPb.setMax(100);
                taskPb.setProgress((int) (percent * 100));
            } else {
                taskPb.setMax(1);
                taskPb.setProgress(0);
            }

            switch (status) {
                case FileDownloadStatus.error:
                    taskStatusTv.setText(R.string.download_adapter_load_failure);
                    break;
                case FileDownloadStatus.paused:
                    taskStatusTv.setText(R.string.download_adpater_pause);
                    break;
                default:
                    taskStatusTv.setText(R.string.download_adapter_not_load);
                    break;
            }
            taskPlayBtn.setImageResource(R.drawable.ic_video_download_play_24dp);
        }

        public void updateDownloading(int status, long soFar, long total) {
            float percent = soFar / (float) total;
            taskPb.setVisibility(View.VISIBLE);
            taskPb.setMax(100);
            taskPb.setProgress((int) (percent * 100));

            switch (status) {
                case FileDownloadStatus.pending:
                    taskStatusTv.setText(R.string.download_adapter_wait_load);
                    break;
                case FileDownloadStatus.started:
                    taskStatusTv.setText(R.string.download_adapter_start_load);
                    break;
                case FileDownloadStatus.connected:
                    taskStatusTv.setText(R.string.download_adapter_connected);
                    break;
                case FileDownloadStatus.progress:
                    taskStatusTv.setText(formatStatus(soFar, total));
                    break;
                default:
                    taskStatusTv.setText(formatStatus(soFar, total));
                    break;
            }
            taskPlayBtn.setImageResource(R.drawable.ic_video_download_pause_24dp);
        }

        private String formatStatus(long soFar, long total) {
            if (soFar > 0 && total > 0) {
                total = total / 1024 / 1024;
                soFar = soFar / 1024 / 1024;
                return soFar + "MB/" + total + "MB";
            }
            return "NA/NA";
        }
    }
}
