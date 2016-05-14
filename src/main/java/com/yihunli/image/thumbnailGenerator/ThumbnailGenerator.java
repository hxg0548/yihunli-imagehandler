package com.yihunli.image.thumbnailGenerator;


import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 * Hello world!
 *
 */
public class ThumbnailGenerator 
{
    private boolean isInitFlag = false;  //对象是否已经初始化
    private String pic_big_pathfilename =null; //定义源图片所在的带路径目录的文件名
    private String pic_small_pathfilename =null;//生成小图片的带存放路径目录的文件名
    private int smallpicwidth = 0; //定义生成小图片的宽度和高度，给其中一个就可以了
    private int smallpicheight = 0;
    private int pic_big_width=0;
    private int pic_big_height = 0;//定义小图片的相比图片的比例
    
    private double picscale = 0; //定义小图片的相比原图片的比例
    
    
    public ThumbnailGenerator(){
    	this.isInitFlag = false;
    }
    
    private void resetJpegToolParams(){
    	this.picscale = 0;
    	this.smallpicwidth = 0;
    	this.smallpicheight = 0;
    	this.isInitFlag = false;
    }
    
    public void SetScale(double scale) throws Exception{
    	if(scale<=0){
    		throw new IllegalArgumentException("缩略图比例不能为0和负数!");
    	}
    	this.resetJpegToolParams();
    	this.picscale = scale;
    	this.isInitFlag = true;
    }
    
    public void SetSmallWidth(int smallpicwidth)throws Exception{
    	if(smallpicwidth<=0){
    		throw new IllegalArgumentException("缩略图的宽度不能为0和负数！ ");
    	}
    	this.resetJpegToolParams();
    	this.smallpicwidth = smallpicwidth;
    	this.isInitFlag = true;
    }
    
    public void SetSmallHeight(int smallpicheight)throws Exception{
    	if(smallpicheight<=0){
    		throw new IllegalArgumentException("缩略图的高度不能为0和负数！");
    	}
    	this.resetJpegToolParams();
    	this.smallpicheight = smallpicheight;
    	this.isInitFlag=true;
    }
    
    public String getpic_big_pathfilename(){
    	return this.pic_big_pathfilename;
    }
    
    public String getpic_small_pathfilename(){
    	return this.pic_small_pathfilename;
    }
    
    public int getsrcw(){
    	return this.pic_big_width;
    }
    
    public int getsrch(){
    	return this.pic_big_height;
    }
    
    /**
     * 生成源图像的缩略图像
     * @param pic_big_pathfilename 原图像文件名，包含路径（如windows下 C:\\pic.jpg; liunx下 /home/..)
     * @param pic_small_pathfilename 生成的缩略图像文件名
     * 
     * throws JpegToolException
     */
    
    public void doFinal(String pic_big_pathfilename,String pic_small_pathfilename)throws Exception{
    	if(!this.isInitFlag){
    		throw new Exception("对象参数没有初始化.");
    	}
    	if(pic_big_pathfilename==null || pic_small_pathfilename==null){
    		throw new Exception("包含文件名的路径为空.");
    	}
    	if((!pic_big_pathfilename.toLowerCase().endsWith("jpg")) &&(!pic_big_pathfilename.toLowerCase().equals("jpeg"))){
    		throw new Exception("只能处理JPG/JPEG文件!");
    	}
    	if((!pic_big_pathfilename.toLowerCase().endsWith("jpg")) &&(!pic_big_pathfilename.toLowerCase().equals("jpeg"))){
    		throw new Exception("只能处理JPG/JPEG文件!");
    	}
    	
    	this.pic_big_pathfilename = pic_big_pathfilename;
    	this.pic_small_pathfilename = pic_small_pathfilename;
    	
    	int smallw = 0;
    	int smallh = 0;
    	
    	File fi = new File(pic_big_pathfilename);
    	
    	File fo = new File(pic_small_pathfilename);

    	AffineTransform transform = new AffineTransform();
    	BufferedImage bsrc = null;
    	
    	try{
    		bsrc = ImageIO.read(fi);
    	}catch(IOException ex){
    		throw new Exception("读取资源文件出错.");
    	}
    	
    	this.pic_big_width=bsrc.getWidth();  //原图像的宽度
    	this.pic_big_height=bsrc.getHeight();
    	double scale = (double)pic_big_width/pic_big_height;//图像的长款比例
    	
    	if(this.smallpicwidth != 0){
    		smallw = this.smallpicwidth; //新生成的缩略图像的宽度
    		smallh = (smallw*pic_big_height)/pic_big_width;
    	}
    	else if(this.smallpicheight != 0){
    		smallh = this.smallpicheight; //新生成的缩略图的长度
    		smallw = (smallh*pic_big_width)/pic_big_height; //新生成的缩略图图像的宽度
    	}
    	else if(this.picscale != 0){
    		smallw = (int)((float)pic_big_width*this.picscale);
    		smallh = (int)((float)pic_big_height*this.picscale);
    	}
    	else{
    		throw new Exception("对象初始化不正确");
    	} 

    	double sx = (double)smallw/pic_big_width;//小/大图像的宽度比例
    	double sy = (double)smallh/pic_big_height;
    	
    	transform.setToScale(sx,sy);//设置图像转换的比例
    	
    	//生成图像转换操作对象
    	AffineTransformOp ato = new AffineTransformOp(transform,null);
    	
    	//生成缩小图像的缓冲对象
    	BufferedImage bsmall = new BufferedImage(smallw,smallh,BufferedImage.TYPE_3BYTE_BGR);
    	ato.filter(bsrc, bsmall);
    	
    	try{
    		ImageIO.write(bsmall,"jpeg", fo);
    	}catch(IOException exl){
    		throw new Exception("写入缩略图文件出错!");
    	}
    }

    
}
