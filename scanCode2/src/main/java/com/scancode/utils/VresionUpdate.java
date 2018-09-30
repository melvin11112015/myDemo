package com.scancode.utils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;

import com.scancode.R;

public class VresionUpdate {
	private String mAPKUrl; // 安装包下载地址
	private Context myContext;
	private Handler handler;
	public static final int DOWNLOAD_FAIL = -1;

	/*
	 * 无更新url可为空
	 */
	public VresionUpdate(Context context, String url, Handler handler) {
		myContext = context;
		mAPKUrl = url;
		this.handler = handler;
	}

	/**
	 * 对话框-提示无版本更新
	 */
	public void showNoUpdateDialog() {
		AlertUtils.alert(
				myContext.getResources().getString(
						R.string.version_update_dialog),
				myContext.getResources().getString(
						R.string.version_no_update_dialog), myContext,
				new UpdateNegative());
	}

	/**
	 * 对话框-提示可取消更新
	 */
	public void showCommonUpdateDialog(String updateValues,
			DialogInterface.OnClickListener updateNegative) {
		AlertUtils.alert(
				myContext.getResources().getString(
						R.string.version_update_dialog), updateValues,
				myContext, new UpdatePositive(), updateNegative);
	}

	/**
	 * 对话框-提示不可取消更新信息
	 */
	public void showMustUpdateDialog(String updateValues,
			String positiveText,
    		String negativeText,
			DialogInterface.OnClickListener negative) {
		AlertUtils.alert(
				myContext.getResources().getString(
						R.string.version_update_dialog), updateValues,
				myContext,positiveText,negativeText, new UpdatePositive(),negative);
	}

	/*
	 * 确认下载新版本
	 */
	public class UpdatePositive implements DialogInterface.OnClickListener {

		@Override
		public void onClick(DialogInterface dialog, int which) {
			// 开始下载
			new startDownload().execute();
		}
	}

	/*
	 * 取消下载新版本
	 */
	public class UpdateNegative implements DialogInterface.OnClickListener {

		@Override
		public void onClick(DialogInterface dialog, int which) {
			dialog.dismiss();
		}
	}

	/*
	 * 开始下载
	 */
	public class startDownload extends AsyncTask<String, Integer, String> {
		private int mProgress = -1; // 下载进度
		private String mApkName = "TrafficePolice.apk";// 安装包名称
		private static final String savePath = "/sdcard/updatedemo/";
		private ProgressDialog progress;
		private static final int DOWNLOADSUCCESS = 1;
		private static final int DOWNLOADFAIL = 2;
		private int downloadState = DOWNLOADSUCCESS;

		@Override
		protected void onPreExecute() {
			progress = new ProgressDialog(myContext);
			progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			progress.setTitle("正在下载！");
			progress.setMax(100);
			progress.setCancelable(false);
			progress.setCanceledOnTouchOutside(false);
			progress.show();
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(String... params) {
			try {
				URL url = new URL(mAPKUrl);
				HttpURLConnection conn = (HttpURLConnection) url
						.openConnection();
				conn.connect();

				int lenghtOfFile = conn.getContentLength();

				File file = new File(savePath);
				if (!file.exists()) {
					file.mkdir();
				}

				InputStream input = new BufferedInputStream(url.openStream());
				OutputStream output = new FileOutputStream(savePath + mApkName);
				byte data[] = new byte[1024];
				long total = 0;
				int count = 0;
				while ((count = input.read(data)) != -1) {
					total += count;
					int progress = (int) ((total * 100) / lenghtOfFile);
					if (progress != mProgress) {
						mProgress = progress;
					}
					// 通知刷新进度条
					publishProgress(mProgress);
					output.write(data, 0, count);
				}

				output.flush();
				output.close();
				input.close();

			} catch (Exception e) {
				// 通知发生错误
				downloadState = DOWNLOADFAIL;
			} finally {
				Thread.currentThread().interrupt();
			}
			return null;
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			progress.setProgress(mProgress);
			super.onProgressUpdate(values);
		}

		@Override
		protected void onPostExecute(String result) {
			if (downloadState == DOWNLOADSUCCESS) {
				// 执行安装程序
				String fileName = savePath + mApkName;
				Intent intent = new Intent(Intent.ACTION_VIEW);
				intent.setDataAndType(Uri.fromFile(new File(fileName)),
						"application/vnd.android.package-archive");
				myContext.startActivity(intent);
			} else {
				AlertUtils.alert("下载出错，稍候重试！", myContext,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
								Message msg = handler.obtainMessage();
								msg.what = DOWNLOAD_FAIL;
								handler.sendMessage(msg);
							}
						});
				progress.dismiss();
			}
			super.onPostExecute(result);
		}
	}
}
