package com.penoder.mylibrary.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.text.TextUtils;
import android.util.Base64;
import android.widget.ImageView;

import com.penoder.mylibrary.refresh.util.DensityUtil;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;

public class BitmapUtil {

    public static Bitmap getBitmapFromPath(String path) {

        if (!new File(path).exists()) {
            System.err.println("getBitmapFromPath: file not exists");
            return null;
        }
        // Bitmap bitmap = Bitmap.createBitmap(1366, 768, Config.ARGB_8888);
        // Canvas canvas = new Canvas(bitmap);
        // Movie movie = Movie.decodeFile(path);
        // movie.draw(canvas, 0, 0);
        //
        // return bitmap;

        byte[] buf = new byte[1024 * 1024];// 1M
        Bitmap bitmap = null;

        try {

            FileInputStream fis = new FileInputStream(path);
            int len = fis.read(buf, 0, buf.length);
            bitmap = BitmapFactory.decodeByteArray(buf, 0, len);
            if (bitmap == null) {
                System.out.println("len= " + len);
                System.err
                        .println("path: " + path + "  could not be decode!!!");
            }
        } catch (Exception e) {
            e.printStackTrace();

        }

        return bitmap;
    }

    /**
     * 图片模糊化
     * 完成上面的代码后，需要在app的 gradle 文件中添加如下的支持：
     * defaultConfig {    ......
     * renderscriptTargetApi 19
     * renderscriptSupportModeEnabled true
     * }
     */
    public static Bitmap blur(Context context, Bitmap image) {
        if (Build.VERSION.SDK_INT >= 17) {
            float BITMAP_SCALE = 0.4f;
            float BLUR_RADIUS = 25f;
            int width = Math.round(image.getWidth() * BITMAP_SCALE);
            int height = Math.round(image.getHeight() * BITMAP_SCALE);
            Bitmap inputBitmap = Bitmap.createScaledBitmap(image, width, height, false);
            Bitmap outputBitmap = Bitmap.createBitmap(inputBitmap);
            RenderScript rs = RenderScript.create(context);
            ScriptIntrinsicBlur blurScript = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
            Allocation tmpIn = Allocation.createFromBitmap(rs, inputBitmap);
            Allocation tmpOut = Allocation.createFromBitmap(rs, outputBitmap);
            blurScript.setRadius(BLUR_RADIUS);
            blurScript.setInput(tmpIn);
            blurScript.forEach(tmpOut);
            tmpOut.copyTo(outputBitmap);
            return outputBitmap;
        } else {
            return null;
        }
    }

    /**
     * convert Bitmap to byte array
     *
     * @param b
     * @return
     */
    public static byte[] bitmapToByte(Bitmap b) {
        if (b == null) {
            return null;
        }

        ByteArrayOutputStream o = new ByteArrayOutputStream();
        b.compress(Bitmap.CompressFormat.PNG, 100, o);
        return o.toByteArray();
    }

    /**
     * convert byte array to Bitmap
     *
     * @param b
     * @return
     */
    public static Bitmap byteToBitmap(byte[] b) {
        return (b == null || b.length == 0) ? null : BitmapFactory.decodeByteArray(b, 0, b.length);
    }

    /**
     * convert Drawable to Bitmap
     *
     * @param d
     * @return
     */
    public static Bitmap drawableToBitmap(Drawable d) {
        return d == null ? null : ((BitmapDrawable) d).getBitmap();
    }

    /**
     * convert Bitmap to Drawable
     *
     * @param b
     * @return
     */
    public static Drawable bitmapToDrawable(Bitmap b) {
        return b == null ? null : new BitmapDrawable(b);
    }

    /**
     * convert Drawable to byte array
     *
     * @param d
     * @return
     */
    public static byte[] drawableToByte(Drawable d) {
        return bitmapToByte(drawableToBitmap(d));
    }

    /**
     * convert byte array to Drawable
     *
     * @param b
     * @return
     */
    public static Drawable byteToDrawable(byte[] b) {
        return bitmapToDrawable(byteToBitmap(b));
    }


    /**
     * scale image
     *
     * @param org
     * @param newWidth
     * @param newHeight
     * @return
     */
    public static Bitmap scaleImageTo(Bitmap org, int newWidth, int newHeight) {
        return scaleImage(org, (float) newWidth / org.getWidth(), (float) newHeight / org.getHeight());
    }

    /**
     * scale image
     *
     * @param org
     * @param scale
     * @return
     */
    public static Bitmap scaleImageTo(Bitmap org, float scale) {
        return scaleImage(org, org.getWidth() * scale, org.getHeight() * scale);
    }

    /**
     * scale image
     *
     * @param org
     * @param scaleWidth  sacle of width
     * @param scaleHeight scale of height
     * @return
     */
    public static Bitmap scaleImage(Bitmap org, float scaleWidth, float scaleHeight) {
        if (org == null) {
            return null;
        }

        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        return Bitmap.createBitmap(org, 0, 0, org.getWidth(), org.getHeight(), matrix, true);
    }

    public static Bitmap reduce(String filePath, int maxWeight) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);
        float realWidth = options.outWidth;
        float realHeight = options.outHeight;
        float larger = (realWidth > realHeight) ? realWidth : realHeight;
        int scale = (int) (larger / maxWeight);
        if (scale <= 0) {
            scale = 1;
        }
        options.inJustDecodeBounds = false;
        options.inSampleSize = scale;
        Bitmap bitmap = BitmapFactory.decodeFile(filePath, options);
        return bitmap;
    }


    public static Bitmap reduce(Context context, String filePath) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);
        int photowidth = options.outWidth;
        int photoheight = options.outHeight;

        //获取比例
        int scale = 1;
        int dx = photowidth / ScreenUtils.getScreenWidth(context);
        int dy = photoheight / ScreenUtils.getScreenHeight(context);
        if (dx > dy && dy > 0) {
            scale = dx;
        } else if (dy > dx && dx > 0) {
            scale = dy;
        }
        options.inJustDecodeBounds = false;
        options.inSampleSize = scale;
        return BitmapFactory.decodeFile(filePath, options);
    }

    // 编码图片文件
    public static String decodeImage(String imagePath) {
        try {
            Bitmap bitmap = reduce(imagePath, 800);
            String bitmapString = bitmapToString(bitmap);
            String encodeString = URLEncoder.encode(bitmapString, "UTF-8");
            bitmap.recycle();
            bitmap = null;
            return encodeString;
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 将Bitmap转换成字符串
     *
     * @param bitmap
     * @return
     */
    public static String bitmapToString(Bitmap bitmap) {
        String string;
        ByteArrayOutputStream bStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, bStream);
        byte[] bytes = bStream.toByteArray();
        string = Base64.encodeToString(bytes, Base64.NO_WRAP);
        return string;
    }

    public static String bitmapToBase64(Bitmap bitmap) {
        // 将输入流转换为byte数组
        byte[] d = getByte(bitmap);
        // 将这个输入流以Base64格式编码为String
        return Base64.encodeToString(d, Base64.NO_WRAP);
    }

    //剪切
    public static Bitmap reduceToDefindedSize(Context context, String filePath, int width, int height) {
        if (TextUtils.isEmpty(filePath)) {
            return null;
        }
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);
        int photowidth = options.outWidth;
        int photoheight = options.outHeight;

        //获取比例
        int scale = 1;
        int dx;
        int dy;
        if (0 == width || 0 == height) {
            dx = photowidth / DensityUtil.dp2px(30);
            dy = photoheight / DensityUtil.dp2px(30);
        } else {
            dx = photowidth / width;
            dy = photoheight / height;
        }


        if (dx > dy && dy > 0) {
            scale = dx;
        } else if (dy > dx && dx > 0) {
            scale = dy;
        }
        options.inJustDecodeBounds = false;
        options.inSampleSize = scale;

        return BitmapFactory.decodeFile(filePath, options);
    }


    public static Bitmap reduces(Context aContext, String filePath) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);
        int photowidth = options.outWidth;
        int photoheight = options.outHeight;

        //获取比例
        int scale = 1;
        int dx = photowidth / aContext.getResources().getDisplayMetrics().widthPixels;
        int dy = photoheight / aContext.getResources().getDisplayMetrics().heightPixels;
        if (dx > dy && dy > 0) {
            scale = dx;
        } else if (dy > dx && dx > 0) {
            scale = dy;
        }
        options.inJustDecodeBounds = false;
        options.inSampleSize = scale;
        return BitmapFactory.decodeFile(filePath, options);
    }

    /**
     * 将输入流转换为byte数组
     *
     * @param in
     * @return
     */
    public static byte[] getByte(Bitmap in) {
        if (in == null) {
            return null;
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            in.compress(Bitmap.CompressFormat.JPEG, 95, out);
            out.flush();
            out.close();
            return out.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.flush();
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }


    // 将base64转换成Bitmap类型
    public static Bitmap stringtoBitmap(File file) {
        Bitmap bitmap = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        options.inPurgeable = true;
        options.inInputShareable = true;
        InputStream in = null;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            byte[] tempbytes = new byte[1024];
            int byteread = 0;
            in = new FileInputStream(file);
            while ((byteread = in.read(tempbytes)) != -1) {
                out.write(tempbytes, 0, byteread);
            }
            in.close();
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                }
            }
        }
        try {
            byte[] bitmapArray;
            bitmapArray = Base64.decode(out.toByteArray(), Base64.NO_WRAP);
            bitmap = BitmapFactory.decodeByteArray(bitmapArray, 0,
                    bitmapArray.length, options);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    /**
     * 图片灰度化
     *
     * @param bmSrc
     * @return
     */
    public static Bitmap bitmap2Gray(Bitmap bmSrc) {
        // 得到图片的长和宽
        int width = bmSrc.getWidth();
        int height = bmSrc.getHeight();

        Bitmap newBitMap = Bitmap.createBitmap(bmSrc, 0, 0, width, height);
        // 创建目标灰度图像
        Bitmap bmpGray = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        // 创建画布
        Canvas c = new Canvas(bmpGray);
        ColorDrawable drawable = new ColorDrawable(Color.WHITE);
        drawable.draw(c);

        Paint paint = new Paint();
        ColorMatrix cm = new ColorMatrix();
        cm.setSaturation(0);
        ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
        paint.setColorFilter(f);
        c.drawBitmap(newBitMap, 0, 0, paint);
        bmSrc.recycle();
        newBitMap.recycle();
        return bmpGray;
    }

    /**
     * 该函数实现对图像进行二值化处理
     *
     * @param graymap
     * @return
     */
    public static Bitmap gray2Binary(Bitmap graymap) {
        //得到图形的宽度和长度
        int width = graymap.getWidth();
        int height = graymap.getHeight();
        Bitmap newBitMap = Bitmap.createBitmap(graymap, 0, 0, width, height);
        //创建二值化图像
        Bitmap binarymap = null;
        binarymap = newBitMap.copy(Bitmap.Config.ARGB_8888, true);
        //依次循环，对图像的像素进行处理
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                //得到当前像素的值
                int col = binarymap.getPixel(i, j);
                //得到alpha通道的值
                int alpha = col & 0xFF000000;
                //得到图像的像素RGB的值
                int red = (col & 0x00FF0000) >> 16;
                int green = (col & 0x0000FF00) >> 8;
                int blue = (col & 0x000000FF);
                // 用公式X = 0.3×R+0.59×G+0.11×B计算出X代替原来的RGB
                int gray = (int) ((float) red * 0.3 + (float) green * 0.59 + (float) blue * 0.11);
                //对图像进行二值化处理
                if (gray <= 95) {
                    gray = 0;
                } else {
                    gray = 255;
                }
                // 新的ARGB
                int newColor = alpha | (gray << 16) | (gray << 8) | gray;
                //设置新图像的当前像素值
                binarymap.setPixel(i, j, newColor);
            }
        }

        if (graymap != null) {
            graymap.recycle();
        }

        if (newBitMap != null) {
            newBitMap.recycle();
        }
        return binarymap;
    }

    /**
     * 进行线性灰度变化
     *
     * @param image
     * @return
     */
    public static Bitmap lineGrey(Bitmap image) {
        //得到图像的宽度和长度
        int width = image.getWidth();
        int height = image.getHeight();
        Bitmap newBitMap = Bitmap.createBitmap(image, 0, 0, width, height);
        //创建线性拉升灰度图像
        Bitmap linegray = null;
        linegray = newBitMap.copy(Bitmap.Config.ARGB_8888, true);
        //依次循环对图像的像素进行处理
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                //得到每点的像素值
                int col = newBitMap.getPixel(i, j);
                int alpha = col & 0xFF000000;
                int red = (col & 0x00FF0000) >> 16;
                int green = (col & 0x0000FF00) >> 8;
                int blue = (col & 0x000000FF);
                // 增加了图像的亮度
//                red = (int) (1.1 * red + 30);
//                green = (int) (1.1 * green + 30);
//                blue = (int) (1.1 * blue + 30);
//                //对图像像素越界进行处理

                int gray = (int) ((float) red * 0.3 + (float) green * 0.59 + (float) blue * 0.11);
                if (red >= 255) {
                    red = 255;
                }

                if (green >= 255) {
                    green = 255;
                }

                if (blue >= 255) {
                    blue = 255;
                }
                // 新的ARGB
//                int newColor = alpha | (red << 16) | (green << 8) | blue;
                int newColor = alpha | (gray << 16) | (gray << 8) | gray;
                //设置新图像的RGB值
                linegray.setPixel(i, j, newColor);
            }
        }
        if (image != null) {
            image.recycle();
        }

        if (newBitMap != null) {
            newBitMap.recycle();
        }
        return linegray;
    }

    /**
     * 通过uri获取图片并进行压缩
     *
     * @param uri
     */
    public static Bitmap getBitmapFormUri(Activity ac, Uri uri) throws FileNotFoundException, IOException {
        InputStream input = ac.getContentResolver().openInputStream(uri);
        BitmapFactory.Options onlyBoundsOptions = new BitmapFactory.Options();
        onlyBoundsOptions.inJustDecodeBounds = true;
        onlyBoundsOptions.inDither = true;//optional
        onlyBoundsOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;//optional
        BitmapFactory.decodeStream(input, null, onlyBoundsOptions);
        input.close();
        int originalWidth = onlyBoundsOptions.outWidth;
        int originalHeight = onlyBoundsOptions.outHeight;
        if ((originalWidth == -1) || (originalHeight == -1))
            return null;
        //图片分辨率以480x800为标准
        float hh = 800f;//这里设置高度为800f
        float ww = 480f;//这里设置宽度为480f
        //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;//be=1表示不缩放
        if (originalWidth > originalHeight && originalWidth > ww) {//如果宽度大的话根据宽度固定大小缩放
            be = (int) (originalWidth / ww);
        } else if (originalWidth < originalHeight && originalHeight > hh) {//如果高度高的话根据宽度固定大小缩放
            be = (int) (originalHeight / hh);
        }
        if (be <= 0) {
            be = 1;
        }
        //比例压缩
        BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
        bitmapOptions.inSampleSize = be;//设置缩放比例
        bitmapOptions.inDither = true;//optional
        bitmapOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;//optional
        input = ac.getContentResolver().openInputStream(uri);
        Bitmap bitmap = BitmapFactory.decodeStream(input, null, bitmapOptions);
        input.close();
        return compressImage(bitmap);//再进行质量压缩
    }

    /**
     * 质量压缩方法
     *
     * @param image
     * @return
     */
    public static Bitmap compressImage(Bitmap image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while (baos.toByteArray().length / 1024 > 300) {  //循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset();//重置baos即清空baos
            //第一个参数 ：图片格式 ，第二个参数： 图片质量，100为最高，0为最差  ，第三个参数：保存压缩后的数据的流
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
            options -= 10;//每次都减少10
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片
        return bitmap;
    }


    //处理图片大小质量
    public static String saveBitmapToFile(File file, String newpath) {
        if (file == null) {
            return "";
        }
        try {

            // BitmapFactory options to downsize the image
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            o.inSampleSize = 8;
            // factor of downsizing the image

            FileInputStream inputStream = new FileInputStream(file);
            //Bitmap selectedBitmap = null;
            BitmapFactory.decodeStream(inputStream, null, o);
            inputStream.close();

            // The new size we want to scale to
            final int REQUIRED_SIZE = 90;

            // Find the correct scale value. It should be the power of 2.
            int scale = 1;
            while (o.outWidth / scale / 2 >= REQUIRED_SIZE &&
                    o.outHeight / scale / 2 >= REQUIRED_SIZE) {
                scale *= 2;
            }

            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            inputStream = new FileInputStream(file);

            Bitmap selectedBitmap = BitmapFactory.decodeStream(inputStream, null, o2);
            inputStream.close();

            File aa = new File(newpath);

            FileOutputStream outputStream = new FileOutputStream(aa);

            //choose another format if PNG doesn't suit you

            selectedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);


            String filepath = aa.getAbsolutePath();

            return filepath;
        } catch (Exception e) {
            return null;
        }
    }

    //防止用户自行清除手机内存时把照片删除导致界面上的异常
    public static void setBitmap(ImageView imageView, Bitmap bitmap) {
        if (bitmap == null) {
            return;
        }
        imageView.setImageBitmap(bitmap);
    }


    public static Bitmap resizeBitmap(Bitmap bm, int newWidth, int newHeight) {
        // 获得图片的宽高
        int width = bm.getWidth();
        int height = bm.getHeight();
        // 计算缩放比例
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // 取得想要缩放的matrix参数
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        // 得到新的图片   www.2cto.com
        Bitmap newbm = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);
        if (bm != null) {
            bm.recycle();
        }
        return newbm;
    }
}
