package com.poison.eagle.utils.qrcode;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.UUID;

import javax.imageio.ImageIO;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.json.JSONObject;

import jp.sourceforge.qrcode.QRCodeDecoder;
import jp.sourceforge.qrcode.exception.DecodingFailedException;

import com.poison.eagle.utils.CheckParams;
import com.poison.eagle.utils.CommentUtils;
import com.swetake.util.Qrcode;



public class QRCodeUtils {
	private static QRCodeUtils qrCodeUtils;
	public QRCodeUtils(){}
	public static QRCodeUtils getInstance(){
		if(qrCodeUtils == null){
			return new QRCodeUtils();
		}else{
			return qrCodeUtils;
		}
	}

	public static String imgType = "png";//二维码图片格式
	private static int size = 8;//二维码尺寸
	private String codeType = "utf-8";
	
	//设置二维码中间图片的宽高
	private int imageWidth = 50;
	private int imageHeight = 50;
 /** 

     * 生成二维码(QRCode)图片 

     * @param content 存储内容 

     * @param imgPath 图片路径 

     */  

//    public void encoderQRCode(String content, String imgPath) {  
//
//        this.encoderQRCode(content, imgPath, "png", 7);   
//
//    }  

      

    /** 

     * 生成二维码(QRCode)图片 

     * @param content 存储内容 

     * @param output 输出流 

     */  

    public void encoderQRCode(String content, OutputStream output) {  

        this.encoderQRCode(content, output, "png", 8);  

    }  

      

    /** 

     * 生成二维码(QRCode)图片 

     * @param content 存储内容 

     * @param imgPath 图片路径 

     * @param imgType 图片类型 

     */  

//    public void encoderQRCode(String content, String imgPath, String imgType) {  
//
//        this.encoderQRCode(content, imgPath, imgType, 7);  
//
//    }  

      

    /** 

     * 生成二维码(QRCode)图片 

     * @param content 存储内容 

     * @param output 输出流 

     * @param imgType 图片类型 

     */  

//    public void encoderQRCode(String content, OutputStream output, String imgType) {  
//
//        this.encoderQRCode(content, output, imgType, 7);  
//
//    }  
    

  

    /** 
     * 生成带有logo的二维码(QRCode)图片（默认尺寸的） 
     * @param content 存储内容 
     * @param imgPath 图片路径 
     * @param logoImg logo
     */  
    public void encoderQRCode(String content, String imgPath, String logoImg) {  
        try {  
            BufferedImage bufImg = this.qRCodeCommon(content, imgType, size); 
            
	        //在二维码中间加入图片
            createPhotoAtCenterFromURL(bufImg , logoImg);
            
            File imgFile = new File(imgPath); 
            // 生成二维码QRCode图片  
            
            
            ImageIO.write(bufImg, imgType, imgFile);  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
    }  
    /** 
     * 生成带有logo的二维码(QRCode)图片（提供尺寸的） 
     * @param content 存储内容 
     * @param imgPath 图片路径 
     * @param logoImg logo
     */  
    public void encoderQRCodeBySize(String content, String imgPath, String logoImg,int size) {  
    	try {  
    		BufferedImage bufImg = this.qRCodeCommon(content, imgType, size); 
    		
    		//在二维码中间加入图片
    		createPhotoAtCenterFromURL(bufImg , logoImg);
    		
    		File imgFile = new File(imgPath); 
    		// 生成二维码QRCode图片  
    		
    		ImageIO.write(bufImg, imgType, imgFile);  
    	} catch (Exception e) {  
    		e.printStackTrace();  
    	}  
    }  
    /** 
     * 生成带有logo的二维码(QRCode)图片（默认尺寸的） 
     * @param content 存储内容 
     * @param imgPath 图片路径 
     * @param logoImg logo
     */  
    public void encoderQRCode(String content, String imgPath, String logoImg,int size , int logoW , int logoH) {  
    	try {  
    		BufferedImage bufImg = this.qRCodeCommon(content, imgType, size); 
    		
    		//在二维码中间加入图片
    		createPhotoAtCenterFromURL(bufImg , logoImg);
    		
    		File imgFile = new File(imgPath); 
    		// 生成二维码QRCode图片  
    		
    		ImageIO.write(bufImg, imgType, imgFile);  
    	} catch (Exception e) {  
    		e.printStackTrace();  
    	}  
    }  
    /** 

     * 生成二维码(QRCode)图片 

     * @param content 存储内容 
     * @param logoImg logo

     * @param size 二维码尺寸 

     */  
    
//    public void encoderQRCode(String content, String logoImg) {  
//    	try {  
//    		BufferedImage bufImg = this.qRCodeCommon(content, imgType, size); 
//    		
//    		//在二维码中间加入图片
//    		createPhotoAtCenterFromURL(bufImg , logoImg);
//    		
//    		File imgFile = new File(savePath); 
//    		// 生成二维码QRCode图片  
//    		
//    		ImageIO.write(bufImg, imgType, imgFile);  
//    	} catch (Exception e) {  
//    		e.printStackTrace();  
//    	}  
//    }  
    /** 

     * 生成二维码(QRCode)图片 

     * @param content 存储内容 

     * @param imgPath 图片路径 

     * @param imgType 图片类型 

     * @param size 二维码尺寸 

     */  
    
    public void getQrcode(String content, String imgPath) {  
    	try {  
    		BufferedImage bufImg = this.qRCodeCommon(content, imgType, size); 
    		System.out.println("传入的二维码的值为"+content+"图片类型为"+imgType+"图片的大小为"+size);
    		//在二维码中间加入图片
//    		createPhotoAtCenter(bufImg);
    		
    		File imgFile = new File(imgPath);  
    		// 生成二维码QRCode图片  
    		ImageIO.write(bufImg, imgType, imgFile);  
    	} catch (Exception e) {  
    		System.out.println("生成二维码方法报错");
    		e.printStackTrace();  
    	}  
    }  

    /** 
     * 生成二维码(QRCode)图片 
     * 
     * @param content 存储内容 
     * @param output 输出流 
     * @param imgType 图片类型 
     * @param size 二维码尺寸 
     */  

    public void encoderQRCode(String content, OutputStream output, String imgType, int size) {  

        try {  

            BufferedImage bufImg = this.qRCodeCommon(content, imgType, size);
            
            //在二维码中间加入图片
//            createPhotoAtCenter(bufImg);
            
            // 生成二维码QRCode图片  
            
            ImageIO.write(bufImg, imgType, output);  

        } catch (Exception e) {  

            e.printStackTrace();  

        }  

    }  

      

    /** 

     * 生成二维码(QRCode)图片的公共方法 

     * @param content 存储内容 

     * @param imgType 图片类型 

     * @param size 二维码尺寸 

     * @return 

     */  

    private BufferedImage qRCodeCommon(String content, String imgType, int size) {  
        BufferedImage bufImg = null;  
        try {  
            Qrcode qrcodeHandler = new Qrcode();  
            // 设置二维码排错率，可选L(7%)、M(15%)、Q(25%)、H(30%)，排错率越高可存储的信息越少，但对二维码清晰度的要求越小  
            qrcodeHandler.setQrcodeErrorCorrect('H');  
            qrcodeHandler.setQrcodeEncodeMode('B'); 
            // 设置设置二维码尺寸，取值范围1-40，值越大尺寸越大，可存储的信息越大  
            qrcodeHandler.setQrcodeVersion(size);
            // 获得内容的字节数组，设置编码格式  
            byte[] contentBytes = content.toString().getBytes(codeType);  
            // 图片尺寸  
            int imgSize = 67 + 12 * (size - 1);  
            bufImg = new BufferedImage(imgSize, imgSize, BufferedImage.TYPE_INT_RGB);  
            Graphics2D gs = bufImg.createGraphics();  
            // 设置背景颜色  
            gs.setBackground(Color.WHITE);  
            gs.clearRect(0, 0, imgSize, imgSize);  
            // 设定图像颜色> BLACK  
            gs.setColor(Color.BLACK);  
            // 设置偏移量，不设置可能导致解析出错  
            int pixoff = 2;  
            // 输出内容> 二维码  
            if (contentBytes.length > 0 && contentBytes.length < 800) {  
                boolean[][] codeOut = qrcodeHandler.calQrcode(contentBytes);  
                for (int i = 0; i < codeOut.length; i++) {  
                    for (int j = 0; j < codeOut.length; j++) {  
                        if (codeOut[j][i]) {  
                            gs.fillRect(j * 3 + pixoff, i * 3 + pixoff, 3, 3);  
                        }  
                    }  
                }  
            } else {  
                throw new Exception("QRCode content bytes length = " + contentBytes.length + " not in [0, 800].");  
            }  
            gs.dispose();  
            bufImg.flush();  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
        return bufImg;  
    }  

    /** 

     * 解析二维码（QRCode） 

     * @param imgPath 图片路径 

     * @return 

     */  

    public String decoderQRCode(String imgPath) {  
        // QRCode 二维码图片的文件  
        File imageFile = new File(imgPath);  
        BufferedImage bufImg = null;  
        String content = null;  
        try {  
            bufImg = ImageIO.read(imageFile);  
            QRCodeDecoder decoder = new QRCodeDecoder();  
            content = new String(decoder.decode(new QRCodeImageUtils(bufImg)), codeType);   
        } catch (IOException e) {  
            System.out.println("Error: " + e.getMessage());  
            e.printStackTrace();  
        } catch (DecodingFailedException dfe) {  
            System.out.println("Error: " + dfe.getMessage());  
            dfe.printStackTrace();  
        }  
        return content;  
    }  

    /** 
     * 解析二维码（QRCode） 
     * 
     * @param input 输入流 
     * @return 
     */  

    public String decoderQRCode(InputStream input) {  
        BufferedImage bufImg = null;  
        String content = null;  
        try {  
            bufImg = ImageIO.read(input);  
            QRCodeDecoder decoder = new QRCodeDecoder();  
            content = new String(decoder.decode(new QRCodeImageUtils(bufImg)), codeType);   
        } catch (IOException e) {  
            System.out.println("Error: " + e.getMessage());  
            e.printStackTrace();  
        } catch (DecodingFailedException dfe) {  
            System.out.println("Error: " + dfe.getMessage());  
            dfe.printStackTrace();  
        }  
        return content;  
    }  
    
    /**
     * 在二维码中间加入图片
     * 
     * @param bugImg
     * @return
     */
    private BufferedImage createPhotoAtCenter(BufferedImage bufImg) throws Exception {
    	 Image im = ImageIO.read(new File(savePath+"IMG_0968.JPG"));
         Graphics2D g = bufImg.createGraphics();
         //获取bufImg的中间位置
         int centerX = bufImg.getMinX() + bufImg.getWidth()/2 - imageWidth/2;
         int centerY = bufImg.getMinY() + bufImg.getHeight()/2 - imageHeight/2;
         g.drawImage(im,centerX,centerY,imageWidth,imageHeight,null);
         g.dispose();
         bufImg.flush();
    	return bufImg;
    }
    /**
     * 在二维码中间加入网络图片
     * 
     * @param bugImg
     * @return
     */
    private BufferedImage createPhotoAtCenterFromURL(BufferedImage bufImg,String logoURlImg) throws Exception {
    	URL url = new URL(logoURlImg);
    	
    	Image im = ImageIO.read(url);// ImageIO.read(new File(logoImg));
    	Graphics2D g = bufImg.createGraphics();
    	//获取bufImg的中间位置
    	int centerX = bufImg.getMinX() + bufImg.getWidth()/2 - imageWidth/2;
    	int centerY = bufImg.getMinY() + bufImg.getHeight()/2 - imageHeight/2;
    	g.drawImage(im,centerX,centerY,imageWidth,imageHeight,null);
    	g.dispose();
    	bufImg.flush();
    	return bufImg;
    }
    
    private static final String logoPath = "http://112.126.68.72/image/common/fd20933e684147ab39e85072837cb033.jpg";
	private static final String savePath =  System.getProperty("user.dir")+java.io.File.separator+"src"+
			java.io.File.separator+"main"+java.io.File.separator+
			"webapp"+java.io.File.separator+"img"+java.io.File.separator;
    public static void main(String[] args) throws IOException {  

        //String imgPath = savePath+UUID.randomUUID()+"."+imgType;  
        String imgPath = "d:\\qrcode\\Mich-ael_QRCode.png";
//        String encoderContent = "http://www.baidu.com"; 
        String encoderContent = "席博强";
//        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
//        encoderContent = br.readLine();
       
       QRCodeUtils qrCodeUtils = QRCodeUtils.getInstance();
       //qrCodeUtils.encoderQRCodeBySize(encoderContent, imgPath, "http://112.126.68.72/image/common/da93f302425b22bb753299789b2e9ea7.png", 8);
//       qrCodeUtils.qRCodeCommon("http://www.baidu.com", "png", 8);
       OutputStream output = new FileOutputStream(imgPath);
       qrCodeUtils.encoderQRCode(encoderContent, output);
       
       
       File targetFile = new File(imgPath);
       String targetURL = "http://p.duyao001.com/qrcode/upload_image_s.do";
       HttpClient client = new HttpClient();
       PostMethod filePost = new PostMethod(targetURL);
       
       try {
    	   
       Part[] parts = { new FilePart("files", targetFile.getName(), targetFile) }; 
       filePost.setRequestEntity(new MultipartRequestEntity(parts,
				filePost.getParams()));
		client.getHttpConnectionManager().getParams()
				.setConnectionTimeout(5000);
		int status = client.executeMethod(filePost);
		if (status == HttpStatus.SC_OK) {
			System.out.println("上传成功");
			//LOG.info("上传成功");
			JSONObject json = new JSONObject(filePost.getResponseBodyAsString());
			imgPath = json.getString("message");
			System.out.println("上传成功后的返回标示为"+json.toString());
			System.out.println("得到的图片地址为"+imgPath);
			imgPath = CheckParams.replaceStringOfJson(imgPath);
			System.out.println("处理后的图片地址为"+imgPath);
			//deleteFile(filePath);
			// 上传成功
		} else {
			System.out.println("上传失败");
			//LOG.error("上传失败");
			// 上传失败
		}
	} catch (Exception ex) {
		ex.printStackTrace();
	} finally {
		filePost.releaseConnection();
	}
//       qrCodeUtils.encoderQRCode(encoderContent, System.out);
        
//        handler.encoderQRCode(encoderContent, imgPath, "png");  

        //String decoderContent = qrCodeUtils.decoderQRCode(imgPath);  
//
//        System.out.println("解析结果如下：");  
//
       // System.out.println(decoderContent);  

    }  

}

