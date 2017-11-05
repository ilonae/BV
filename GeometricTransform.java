// BV Ue2 WS2017/18 Vorgabe
//
// Copyright (C) 2017 by Klaus Jung
// All rights reserved.
// Date: 2017-07-15

package bv_ws1718;

public class GeometricTransform {

	public enum InterpolationType {
		NEAREST("Nearest Neighbour"), BILINEAR("Bilinear");

		private final String name;

		private InterpolationType(String s) {
			name = s;
		}

		public String toString() {
			return this.name;
		}
	};

	public void perspective(RasterImage src, RasterImage dst, double angle, double perspectiveDistortion,
			InterpolationType interpolation) {
		switch (interpolation) {
		case NEAREST:
			perspectiveNearestNeighbour(src, dst, angle, perspectiveDistortion);
			break;
		case BILINEAR:
			perspectiveBilinear(src, dst, angle, perspectiveDistortion);
			break;
		default:
			break;
		}

	}

	/**
	 * @param src
	 *            source image
	 * @param dst
	 *            destination Image
	 * @param angle
	 *            rotation angle in degrees
	 * @param perspectiveDistortion
	 *            amount of the perspective distortion
	 */
	public void perspectiveNearestNeighbour(RasterImage src, RasterImage dst, double angle,
			double perspectiveDistortion) {
		dst.width = src.width;
		dst.height = src.height;

		for (int xd = 0; xd < dst.width; xd++) {
			for (int yd = 0; yd < dst.height; yd++) {

				int xdnew = xd - (dst.width / 2);
				int ydnew = yd - (dst.height / 2);

				double xs = (xdnew / (Math.cos(Math.toRadians(angle))
						- xdnew * perspectiveDistortion * Math.sin(Math.toRadians(angle))));
				double ys = (ydnew * (perspectiveDistortion * Math.sin(Math.toRadians(angle)) * xs + 1));

				int xsnew = (int) Math.round((xs + (src.width / 2)));
				int ysnew = (int) Math.round((ys + (src.width / 2)));

				int index = toIndex(xsnew, ysnew, dst);

				if (index < src.argb.length && index >= 0) {
					dst.argb[yd * dst.width + xd]=src.argb[ysnew*src.width+xsnew];
				}
				
				if (xsnew < 0 || xsnew >= src.width || ysnew < 0 || ysnew >= src.height) {
					dst.argb[toIndex(xd,yd,dst)] = (0xFF << 24) | (255<< 16) | (255<< 8) | (255);
				}
			}
		}

		// TODO: implement the geometric transformation using nearest neighbour
		// image rendering

		// NOTE: angle contains the angle in degrees, whereas Math trigonometric
		// functions need the angle in radians

	}

	private int toIndex(int xsnew, int ysnew, RasterImage dst) {
		// TODO Auto-generated method stub
		return (ysnew * dst.width) + xsnew;
	}

	/**
	 * @param src
	 *            source image
	 * @param dst
	 *            destination Image
	 * @param angle
	 *            rotation angle in degrees
	 * @param perspectiveDistortion
	 *            amount of the perspective distortion
	 */
	public void perspectiveBilinear(RasterImage src, RasterImage dst, double angle, double perspectiveDistortion) {
		// TODO: implement the geometric transformation using bilinear
		// interpolation

		// NOTE: angle contains the angle in degrees, whereas Math trigonometric
		// functions need the angle in radians

		dst.width = src.width;
		dst.height = src.height;
		
		int r,g,b=0;

		for (int xd = 0; xd < dst.width; xd++) {
			for (int yd = 0; yd < dst.height; yd++) {

				int xdnew = xd - (dst.width / 2);
				int ydnew = yd - (dst.height / 2);
				
				double xs = (xdnew / (Math.cos(Math.toRadians(angle))
						- xdnew * perspectiveDistortion * Math.sin(Math.toRadians(angle))));
				double ys = (ydnew * (perspectiveDistortion * Math.sin(Math.toRadians(angle)) * xs + 1));
				
				int xsnew = (int) Math.round((xs + (src.width / 2)));
				int ysnew = (int) Math.round((ys + (src.width / 2)));
				
				int x=(int) Math.floor(xsnew);
				int y=(int) Math.floor(ysnew);
				
				
				if (x < -2 || x >= src.width || y < -2 || y >= src.height) {
					dst.argb[toIndex(xd,yd,dst)] = (0xFF << 24) | (255<< 16) | (255<< 8) | (255);
				}
				else if(x<0||y<0||x>=src.width||y>=src.height){
					dst.argb[toIndex(xd,yd,dst)] = (0xFF << 24) | (255<< 16) | (255<< 8) | (255);
				}
				else{
					
					int lt=src.argb[(y) * src.width + (x)];
					int rt=src.argb[(y) * src.width + (x+1)];
					int lb=src.argb[(y+1) * src.width + (x)];
					int rb=src.argb[(y+1) * src.width + (x+1)];
					
					r=255;
					g=255;
					 dst.argb[toIndex(xd,yd,dst)] = (0xff << 24) | (r << 16) | (g << 8) | b;
					 
				}
				}
			}
		}
	}

