package kz.slum.disappointment;

import com.sun.istack.internal.NotNull;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Start {

        private static final int SIZE = 255;

        public static void main(String[] args) {
                new Start().getFiles("/home/user/Desktop/1");
        }

        private void getFiles(@NotNull String path){

                File folder = new File(path);
                File[] Filelist = folder.listFiles();

                for (File file: Filelist) {
                        try {
                                BufferedImage img = ImageIO.read(file);
                                img = formatImage(img);
                                pixelToText(img);
                        } catch (IOException e) {
                                e.printStackTrace();
                        }
                }
        }

        private void pixelToText(BufferedImage img){

                int width = img.getWidth();
                int height = img.getHeight();

                int range = 210;
                File outputfile = new File("/home/user/Desktop/1/boutrrrimage.jpg");

                for (int i = 0; i < width; i++) {
                        for (int k = 0; k < height; k++) {

                                Color c = new Color(img.getRGB(i, k));

                                if (c.getRed() > range && c.getGreen() > range && c.getBlue() > range){
                                        img.setRGB(i, k, Color.WHITE.getRGB());
                                } else {
                                        img.setRGB(i, k, Color.BLACK.getRGB());

                                }

                                System.out.println(c.getRed() + " " + c.getGreen() + " " + c.getBlue());
                        }
                }

                try {
                        ImageIO.write(img, "jpg", outputfile);
                } catch (IOException e) {
                        e.printStackTrace();
                }



        }

        private BufferedImage formatImage(BufferedImage img){
                return resize(img, SIZE, SIZE);
        }

        public static BufferedImage resize(BufferedImage img, int newW, int newH) {
                Image tmp = img.getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
                BufferedImage dimg = new BufferedImage(newW, newH, BufferedImage.TYPE_BYTE_GRAY);

                Graphics2D g2d = dimg.createGraphics();
                g2d.drawImage(tmp, 0, 0, null);
                g2d.dispose();

                return dimg;
        }

}
