package com.zz.testword.util;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

public class FileUtil {
	private final static String TAG = "FileUtil";

	public static String getFileName(String pathandname) {
		int start = pathandname.lastIndexOf("/");
		int end = pathandname.lastIndexOf(".");
		if (start != -1 && end != -1) {
			return pathandname.substring(start + 1, end);
		} else {
			return "";
		}
	}
	private static final String SEPARATOR = File.separator;//路径分隔符
	/**
	**
			* 复制res/raw中的文件到指定目录
    * @param context 上下文
    * @param id 资源ID
    * @param fileName 文件名
    * @param storagePath 目标文件夹的路径
    */
	public static void copyFilesFromRaw(Context context, int id, String fileName,String storagePath){
		InputStream inputStream=context.getResources().openRawResource(id);
		File file = new File(storagePath);
		if (!file.exists()) {//如果文件夹不存在，则创建新的文件夹
			file.mkdirs();
		}
		readInputStream(storagePath + SEPARATOR + fileName, inputStream);
	}
	public static String getString(InputStream inputStream) {
		InputStreamReader inputStreamReader = null;
		try {
			inputStreamReader = new InputStreamReader(inputStream, "utf-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		BufferedReader reader = new BufferedReader(inputStreamReader);
		StringBuffer sb = new StringBuffer("");
		String line;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line);
				sb.append("\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return sb.toString();
	}
	/**
	 * 读取输入流中的数据写入输出流
	 *
	 * @param storagePath 目标文件路径
	 * @param inputStream 输入流
	 */
	public static void readInputStream(String storagePath, InputStream inputStream) {
		File file = new File(storagePath);
		try {
			if (!file.exists()) {
				// 1.建立通道对象
				FileOutputStream fos = new FileOutputStream(file);
				// 2.定义存储空间
				byte[] buffer = new byte[inputStream.available()];
				// 3.开始读文件
				int lenght = 0;
				while ((lenght = inputStream.read(buffer)) != -1) {// 循环从输入流读取buffer字节
					// 将Buffer中的数据写到outputStream对象中
					fos.write(buffer, 0, lenght);
				}
				fos.flush();// 刷新缓冲区
				// 4.关闭流
				fos.close();
				inputStream.close();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}


	}
	public static void inputstreamtofile(InputStream ins,File file){
		try {
			OutputStream os = new FileOutputStream(file);
			int bytesRead = 0;
			byte[] buffer = new byte[8192];
			while ((bytesRead = ins.read(buffer, 0, 8192)) != -1) {
				os.write(buffer, 0, bytesRead);
			}
			ins.close();
			os.close();

		}catch (Exception e){
			Log.i("qwe",e.toString());
		}

	}
	/**
	 *  从assets目录中复制整个文件夹内容
	 *  @param  context  Context 使用CopyFiles类的Activity
	 *  @param  oldPath  String  原文件路径  如：/aa
	 *  @param  newPath  String  复制后路径  如：xx:/bb/cc
	 */
	public static void copyFilesFassets(Context context,String oldPath,String newPath) {
		try {
			String fileNames[] = context.getAssets().list(oldPath);//获取assets目录下的所有文件及目录名
			if (fileNames.length > 0) {//如果是目录
				File file = new File(newPath);
				file.mkdirs();//如果文件夹不存在，则递归
				for (String fileName : fileNames) {
					copyFilesFassets(context, oldPath + "/" + fileName, newPath + "/" + fileName);
				}
			} else {//如果是文件
				InputStream is = context.getAssets().open(oldPath);
				FileOutputStream fos = new FileOutputStream(new File(newPath));
				byte[] buffer = new byte[1024];
				int byteCount = 0;
				while ((byteCount = is.read(buffer)) != -1) {//循环从输入流读取 buffer字节
					fos.write(buffer, 0, byteCount);//将读取的输入流写入到输出流
				}
				fos.flush();//刷新缓冲区
				is.close();
				fos.close();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			//如果捕捉到错误则通知UI线程
		}
	}
		/**
         * 根据行读取内容
         * @return
         */
	public static List<String> Txt(Context context,String filePath) {
		//将读出来的一行行数据使用List存储

		List newList=new ArrayList<String>();
		try {
			File file = new File(filePath);
			int count = 0;//初始化 key值
			if (file.isFile() && file.exists()) {//文件存在
				InputStreamReader isr = new InputStreamReader(new FileInputStream(file));
				BufferedReader br = new BufferedReader(isr);
				String lineTxt = null;
				while ((lineTxt = br.readLine()) != null) {
					if (!"".equals(lineTxt)) {
						String reds = lineTxt.split("\\+")[0];  //java 正则表达式
						newList.add(count, reds);
						count++;
					}
				}
				isr.close();
				br.close();
			}else {
				Toast.makeText(context,"can not find file",Toast.LENGTH_SHORT).show();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return newList;
	}
	public static String createFile(String dir_name, String file_name) {
		String sdcard_path = Environment.getExternalStorageDirectory().getAbsolutePath();
		String dir_path = String.format("%s/Download/%s", sdcard_path, dir_name);
		String file_path = String.format("%s/%s", dir_path, file_name);
		try {
			File dirFile = new File(dir_path);
			if (!dirFile.exists()) {
				dirFile.mkdir();
			}
			File myFile = new File(file_path);
			myFile.createNewFile();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return file_path;
	}

	public static ZipEntry getPicEntry(ZipFile docxFile, int pic_index) {
		String entry_jpg = "word/media/image" + pic_index + ".jpeg";
		String entry_png = "word/media/image" + pic_index + ".png";
		String entry_gif = "word/media/image" + pic_index + ".gif";
		String entry_wmf = "word/media/image" + pic_index + ".wmf";
		ZipEntry pic_entry = null;
		pic_entry = docxFile.getEntry(entry_jpg);
		// 以下为读取docx的图片 转化为流数组
		if (pic_entry == null) {
			pic_entry = docxFile.getEntry(entry_png);
		}
		if (pic_entry == null) {
			pic_entry = docxFile.getEntry(entry_gif);
		}
		if (pic_entry == null) {
			pic_entry = docxFile.getEntry(entry_wmf);
		}
		return pic_entry;
	}

	public static byte[] getPictureBytes(ZipFile docxFile, ZipEntry pic_entry) {
		byte[] pictureBytes = null;
		try {
			InputStream pictIS = docxFile.getInputStream(pic_entry);
			ByteArrayOutputStream pOut = new ByteArrayOutputStream();
			byte[] b = new byte[1000];
			int len = 0;
			while ((len = pictIS.read(b)) != -1) {
				pOut.write(b, 0, len);
			}
			pictIS.close();
			pOut.close();
			pictureBytes = pOut.toByteArray();
			Log.d(TAG, "pictureBytes.length=" + pictureBytes.length);
			if (pictIS != null) {
				pictIS.close();
			}
			if (pOut != null) {
				pOut.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return pictureBytes;

	}

	public static void writePicture(String pic_path, byte[] pictureBytes) {
		File myPicture = new File(pic_path);
		try {
			FileOutputStream outputPicture = new FileOutputStream(myPicture);
			outputPicture.write(pictureBytes);
			outputPicture.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
