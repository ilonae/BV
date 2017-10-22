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

		int half = kernelSize / 2;

		for (int y = 0; y < src.height; y++) {
			for (int x = 0; x < src.width; x++) {

				int posnew = 0;

				for (int i = -half; i <= half; i++) {
					for (int j = -half; j <= half; j++) {

						int pos = (y + i) * src.width + (x + j);
						int border = 0;

						switch (borderProcessing) {

						case WHITE:
							if (x + j < 0 || x + j >= src.width || y + i < half || y + i >= src.height) {
								border = 255;
							} else {
								border = src.argb[pos] & 0xff;
							}
							break;

						case CONTINUE:
							if (x + j < 0 || x + j >= src.width || y + i < half || y + i >= src.height) {
								border = src.argb[y * src.width + x] & 0xff;
							} else {
								border = src.argb[pos] & 0xff;
							}
							break;
						}
						posnew = posnew + border;
					}
				}

				posnew = posnew / (kernelSize * kernelSize);

				int dstval = (0xFF << 24) | (posnew << 16) | (posnew << 8) | posnew;

				int dstpos = y * dst.width + x;
				dst.argb[dstpos] = dstval;
			}
		}
	}

	public void median(RasterImage src, RasterImage dst, int kernelSize, BorderProcessing borderProcessing) {

		for (int yIndex = 0; yIndex < src.argb.length / src.width; yIndex++) {
			for (int xIndex = 0; xIndex < src.argb.length / src.height; xIndex++) {
				int[] colorValues = new int[kernelSize * kernelSize];

				for (int kernelX = 0; kernelX < kernelSize; kernelX++) {
					for (int kernelY = 0; kernelY < kernelSize; kernelY++) {
						int yCoord = ((yIndex - ((kernelSize - 1) / 2)) + kernelY) * src.width;
						int xCoord = (xIndex - ((kernelSize - 1) / 2)) + kernelX;

						int finalIndex = yCoord + xCoord;
						int red = 0;

						if (yCoord < src.argb.length && yCoord >= 0 & xCoord < src.width & xCoord >= 0) {
							red = src.argb[finalIndex] >> 16 & 0xFF;
						} else if (borderProcessing.name == "Border: White") {
							red = 255;
						} else if (borderProcessing.name == "Border: Constant Continuation") {
							while (yCoord >= src.argb.length - src.width) {
								yCoord--;
							}

							while (yCoord < 0) {
								yCoord++;
							}

							while (xCoord < 0) {
								xCoord++;
							}

							while (xCoord >= src.width) {
								xCoord--;
							}

							finalIndex = yCoord + xCoord;
							red = src.argb[finalIndex] >> 16 & 0xFF;
						}

						colorValues[kernelY * (kernelSize - 1) + kernelX] = red;

					}
				}

				Arrays.sort(colorValues);

				int median = colorValues[(colorValues.length - 1) / 2];

				dst.argb[yIndex * src.width + xIndex] = (0xFF << 24) | (median << 16) | (median << 8) | (median);

			}
		}

	}

}
