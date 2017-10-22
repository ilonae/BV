// BV Ue1 WS2017/18 Vorgabe
//
// Copyright (C) 2017 by Klaus Jung
// All rights reserved.
// Date: 2017-07-15

package bv_ws1718;

import java.util.Arrays;

public class Filter {

	public enum FilterType {
		COPY("Copy Image"), BOX("Box Filter"), MEDIAN("Median Filter");

		private final String name;

		private FilterType(String s) {
			name = s;
		}

		public String toString() {
			return this.name;
		}
	};

	public enum BorderProcessing {
		CONTINUE("Border: Constant Continuation"), WHITE("Border: White");

		private final String name;

		private BorderProcessing(String s) {
			name = s;
		}

		public String toString() {
			return this.name;
		}
	};

	// filter implementations go here:

	public void copy(RasterImage src, RasterImage dst) {
		dst.width = src.width;
		dst.height = src.height;
		// TODO: just copy the image
		System.arraycopy(src.argb, 0, dst.argb, 0, src.argb.length);
	}

	public void box(RasterImage src, RasterImage dst, int kernelSize, BorderProcessing borderProcessing) {

		int[] box = new int[kernelSize * kernelSize];
		int half = kernelSize / 2;
		
		for (int y = 0; y < src.height; y++) {
			for (int x = 0; x < src.width; x++) {

				int posnew = 0; 

				for (int i = 0; i < kernelSize; i++) {
					for (int j = 0; j < kernelSize; j++) {
						
						int pos = y * src.width + x;

						switch (borderProcessing) { 
						
						case WHITE:
							if (x < half || x >= src.width - half || y < half || y >= src.height - half) {
								int pixelValue = src.argb[pos];
								box[posnew] = 0xffffffff;
								} 
							else {
								pos = (y + i - half) * src.width + (x + j - half);
								int pixelValue = src.argb[pos];
								box[posnew] = pixelValue;
							}
							break;
								
						case CONTINUE:
							if (x < half || x >= src.width - half || y < half || y >= src.height - half) { 
								int pixelValue = src.argb[pos];
								box[posnew] = pixelValue;
								}
							else {
								pos = (y + i - half) * src.width + (x + j - half); 
								int pixelValue = src.argb[pos];
								box[posnew] = pixelValue;
							}
							break;
						}
						posnew++;
					}
				}

				int rn = 0, gn = 0, bn = 0;
				for (int f = 0; f < box.length; f++) {

					int r = (box[f] >> 16) & 0xff;
					int g = (box[f] >> 8) & 0xff;
					int b = box[f] & 0xff;

					rn += r;
					gn += g;
					bn += b;
				}
				rn = (int) Math.round(rn / box.length);
				gn = (int) Math.round(gn / box.length);
				bn = (int) Math.round(bn / box.length);

				int dstval = (0xFF << 24) | (rn << 16) | (gn << 8) | bn;

				int dstpos = y * dst.width + x;
				dst.argb[dstpos] = dstval;
			}
		}
	}

	public void median(RasterImage src, RasterImage dst, int kernelSize, BorderProcessing borderProcessing) {
		// TODO: implement a median filter with given kernel size and border
		// processing
	}

}
