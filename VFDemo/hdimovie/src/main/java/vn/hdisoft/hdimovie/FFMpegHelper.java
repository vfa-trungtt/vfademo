package vn.hdisoft.hdimovie;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.graphics.RectF;
import android.media.MediaMetadataRetriever;
import android.os.Environment;
import android.util.Log;

import com.github.hiteshsondhi88.libffmpeg.ExecuteBinaryResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.FFmpeg;
import com.github.hiteshsondhi88.libffmpeg.LoadBinaryResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegCommandAlreadyRunningException;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegNotSupportedException;

import vn.hdisoft.hdilib.utils.LogUtils;

public class FFMpegHelper {
    public interface OnProccessVideoListener{
        public void onProcessDone(int errorCode,String errorMessage);
    }
    private FFmpeg ffmpeg;


    private int  errorCode = 0;

    private OnProccessVideoListener _listener;

    public void setOnProcessVideo(OnProccessVideoListener listener){
        _listener = listener;
    }

    public  FFMpegHelper(Context context){
        try {
            if (ffmpeg == null) {
                ffmpeg = FFmpeg.getInstance(context);
            }
            ffmpeg.loadBinary(new LoadBinaryResponseHandler() {
                @Override
                public void onFailure() {
                }

                @Override
                public void onSuccess() {
                    LogUtils.info("load ffmpeg successfull.");
                }
            });
        } catch (FFmpegNotSupportedException e) {
//            showUnsupportedExceptionDialog();
        } catch (Exception e) {
//            Log.d(TAG, "EXception no controlada : " + e);
        }
    }

    private void execFFmpegBinary(final String[] command) {
        try {
            ffmpeg.execute(command, new ExecuteBinaryResponseHandler() {
                @Override
                public void onFailure(String s) {
//                    Log.d(TAG, "FAILED with output : " + s);
                }

                @Override
                public void onSuccess(String s) {
                    Log.d("ffmpeg", "SUCCESS with output : " + s);
                    errorCode = 0;
                }

                @Override
                public void onProgress(String s) {
                    Log.d("ffmpeg", "Started command : ffmpeg " + command);
//                    if (choice == 8)
//                        progressDialog.setMessage("progress : splitting video " + s);
//                    else if (choice == 9)
//                        progressDialog.setMessage("progress : reversing splitted videos " + s);
//                    else if (choice == 10)
//                        progressDialog.setMessage("progress : concatenating reversed videos " + s);
//                    else
//                        progressDialog.setMessage("progress : " + s);
//                    Log.d(TAG, "progress : " + s);
                }

                @Override
                public void onStart() {
                    Log.d("ffmpeg", "Started command : ffmpeg " + command);
//                    progressDialog.setMessage("Processing...");
//                    progressDialog.show();
                }

                @Override
                public void onFinish() {
//                    Log.d(TAG, "Finished command : ffmpeg " + command);
//                    if (choice != 8 && choice != 9 && choice != 10) {
//                        progressDialog.dismiss();
//                    }
                    if(_listener != null) _listener.onProcessDone(errorCode,"");
                }
            });
        } catch (FFmpegCommandAlreadyRunningException e) {
            errorCode = 1;
            Log.e("ffmpeg", "exception :" + e.toString());
        }
    }

    public void cropVideo(String srcPath, String cropInfo,String destPath){
//        cropInfo = "crop=0:0:200:100";
        String[] complexCommand = { "-i", srcPath,"-filter:v", cropInfo, "-c:a","copy", destPath};
        execFFmpegBinary(complexCommand);
    }
    public void cropVideo(String srcPath, RectF rectF, String destPath){
        String cropInfo = "crop="+rectF.width()+":"+rectF.height()+":"+rectF.top+":"+rectF.left+"";
        String[] complexCommand = { "-i", srcPath,"-filter:v", cropInfo, "-c:a","copy", destPath};
        execFFmpegBinary(complexCommand);
    }

    public void addWatermark(String srcMoviePath,String destMoviePath,String filePathWatermark){
//./ffmpeg -i sample.mp4 -i water_mark.png -filter_complex "[1:v]scale=256:256 [ovrl]","[0:v][ovrl] overlay=0:0:" -c:a copy output_watermark.mp4
        MediaMetadataRetriever metaRetriever = new MediaMetadataRetriever();
        metaRetriever.setDataSource(srcMoviePath);
        String height = metaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT);
        String width = metaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH);
        String scaleWatermark = width+":"+height;
//        String[] complexCommand = { "-i", srcMoviePath,"-i",filePathWatermark,
//                "-filter_complex","[1:v]scale=256:256 [ovrl],[0:v][ovrl] overlay=0:0:", "-c:a","copy", destMoviePath};
        String[] complexCommand = { "-i", srcMoviePath,"-i",filePathWatermark,
                "-filter_complex","[1:v]scale="+scaleWatermark+" [ovrl],[0:v][ovrl] overlay=0:0:", "-c:a","copy", destMoviePath};
        execFFmpegBinary(complexCommand);
    }
}
