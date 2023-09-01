import edu.princeton.cs.algs4.Picture;
import edu.princeton.cs.algs4.Stopwatch;

public class SeamCarver {

    // the width of the picture
    private int width;
    // height of the picture
    private int height;

    // checks if we are checking for a horizontal seam or not.
    private boolean horizontal;


    // stores the picture
    private Picture picture;
    
    // private int[][] rgb;
    // private double[][] energy;
    // private double[][] distTo;
    // private double distSink;

    // private int[][] edgeTo;
    // private int edgeSink;

    // create a seam carver object based on the given picture
    public SeamCarver(Picture picture) {
        if (picture == null) {
            throw new IllegalArgumentException();
        }
        this.width = picture.width();
        this.height = picture.height();


        this.picture = new Picture(picture);

        // for (int h = 0; h < height; h++) {
        //     for (int w = 0; w < width; w++) {
        //         energy[h][w] = pixelEnergy(w, h);
        //     }
        // }

    }

    // computes the color gradient of a specified pixel
    private double color(int a, int b) {
        // double blue = Math.pow(a.getBlue() - b.getBlue(), 2);
        // double green = Math.pow(a.getGreen() - b.getGreen(), 2);
        // double red = Math.pow(a.getRed() - b.getRed(), 2);
        // double col = blue + green + red;

        // computed gradients for each color.
        int red = ((a >> 16) & 0xFF) - ((b >> 16) & 0xFF);
        int green = ((a >> 8) & 0xFF) - ((b >> 8) & 0xFF);
        int blue = ((a >> 0) & 0xFF) - ((b >> 0) & 0xFF);

        double col = red * red + green * green + blue * blue;

        return col;

    }

    // returns the pixel energy of a specified pixel
    private double pixelEnergy(int x, int y) {
        int up;
        int down;
        int left;
        int right;

        if (y == 0) {
            down = height - 1;
        }
        else {
            down = y - 1;
        }
        if (y == height - 1) {
            up = 0;
        }
        else {
            up = y + 1;
        }

        if (x == 0) {
            left = width - 1;
        }
        else {
            left = x - 1;
        }
        if (x == width - 1) {
            right = 0;
        }
        else {
            right = x + 1;
        }

        int rgbUp = picture.getRGB(x, up);
        int rgbDown = picture.getRGB(x, down);
        int rgbRight = picture.getRGB(right, y);
        int rgbLeft = picture.getRGB(left, y);


        return Math.sqrt(color(rgbUp, rgbDown) + color(rgbLeft, rgbRight));

    }

    // current picture
    public Picture picture() {

        return new Picture(this.picture);
    }

    // width of current picture
    public int width() {
        return this.width;
    }

    // height of current picture
    public int height() {
        return this.height;
    }

    // energy of pixel at column x and row y
    public double energy(int x, int y) {
        if (x < 0 || x > width || y < 0 || y > height) {
            throw new IllegalArgumentException();
        }
        double energySquared = pixelEnergy(x, y);
        return energySquared;

    }

    // starts the search which relaxes all the energy pixels into infinity but
    // the start rows equal the energy
    // returns back the modified distTo array
    private double[][] startsearch(double[][] dist, double[][] energy) {

        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                if (row == 0) {
                    dist[col][0] = energy[col][0];
                }
                else {
                    dist[col][row] = Double.POSITIVE_INFINITY;
                }
            }
        }

        return dist;
    }

    // relaxes the nodes that are not the end by comparing distTo Values
    private void relaxNotEnd(int i, int j, int x, int y,
                             double[][] energy, double[][] distTo, int[][] edgeTo) {
        if (distTo[x][y] > distTo[i][j] + energy[x][y]) {
            distTo[x][y] = distTo[i][j] + energy[x][y];

            edgeTo[x][y] = i;


        }
    }


    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam() {
        transpose();
        int[] hseam = findVerticalSeam();
        transpose();
        return hseam;
    }

    // populates the seam
    private void populateSeam(int[] seam, int edgeSink, int[][] edgeTo) {

        // System.out.println(edgeSink);
        seam[height - 1] = edgeSink;
        for (int i = height - 2; i >= 0; i--) {
            seam[i] = edgeTo[seam[i + 1]][i + 1];
        }
    }

    // transposes the image whenever function is called
    private void transpose() {

        Picture tmp = new Picture(height, width);
        for (int col = 0; col < width; col++) {
            for (int row = 0; row < height; row++) {

                tmp.setRGB(row, col, this.picture.getRGB(col, row));
            }
        }
        int tmpval = width;
        width = height;
        height = tmpval;
        this.picture = tmp;

    }

    // sequence of indices for vertical seam
    public int[] findVerticalSeam() {
        // see readMe section for explanation

        horizontal = true;

        double[][] distTo = new double[width][height];
        int[][] edgeTo = new int[width][height];


        double[][] energy = new double[width][height];


        for (int w = 0; w < width; w++) {
            for (int h = 0; h < height; h++) {
                energy[w][h] = energy(w, h);
            }
        }
        startsearch(distTo, energy);


        for (int row = 0; row < height - 1; row++) {
            for (int col = 0; col < width; col++) {
                // if (i + j < height()) {
                //     break;
                // }

                relaxNotEnd(col, row, col, row + 1, energy, distTo, edgeTo);
                if (col - 1 >= 0) {
                    relaxNotEnd(col, row, col - 1, row + 1, energy, distTo, edgeTo);
                }
                if (col + 1 < width) {
                    relaxNotEnd(col, row, col + 1, row + 1, energy, distTo, edgeTo);
                }
                // traverse(col, row, energy, distTo, edgeTo);
            }
        }

        double min = distTo[0][height - 1];
        int y = 0;
        for (int i = 1; i < width; i++) {
            if (distTo[i][height - 1] < min) {
                min = distTo[i][height - 1];
                // System.out.println(y);
                y = i;
            }
        }


        int[] seam = new int[height];

        // System.out.println(y);
        populateSeam(seam, y, edgeTo);


        return seam;
    }

    // checks the validity of the seam
    private void assertSeam(int[] seam) {
        if (horizontal) {
            if (seam == null) {
                throw new IllegalArgumentException();
            }
            if (seam.length != width()) {
                throw new IllegalArgumentException();
            }
            if (height() <= 1) {
                throw new IllegalArgumentException();
            }
            int pixelIndex = seam[0];
            for (int pixIn : seam) {
                if (Math.abs(pixIn - pixelIndex) > 1) {
                    throw new IllegalArgumentException();
                }
                if (pixIn >= height() || pixIn < 0) {
                    throw new IllegalArgumentException();
                }
                pixelIndex = pixIn;
            }
        }
        else {
            if (seam == null) {
                throw new IllegalArgumentException();
            }
            if (seam.length != height()) {
                throw new IllegalArgumentException();
            }
            if (width() <= 1) {
                throw new IllegalArgumentException();
            }
            int pixelIndex = seam[0];
            for (int pixIn : seam) {
                if (Math.abs(pixIn - pixelIndex) > 1) {
                    throw new IllegalArgumentException();
                }
                if (pixIn >= width() || pixIn < 0) {
                    throw new IllegalArgumentException();
                }
                pixelIndex = pixIn;
            }
        }


    }


    // remove horizontal seam from current picture
    public void removeHorizontalSeam(int[] seam) {
        horizontal = true;
        assertSeam(seam);
        transpose();
        removeVerticalSeam(seam);
        transpose();

    }

    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] seam) {
        horizontal = false;
        assertSeam(seam);

        Picture removedSeam = new Picture(width - 1, height);

        // this for loop would iterate through the picture and keep track of
        // seam values per row that it would skip over.
        for (int row = 0; row < height; row++) {
            int s = seam[row];
            int numberSkips = 0;
            for (int col = 0; col + numberSkips < width; col++) {
                if (col == s) {
                    numberSkips = 1;
                    if (col == width - 1) {
                        break;
                    }
                }
                removedSeam.setRGB(col, row, picture.getRGB(col + numberSkips, row));
            }
        }

        picture = removedSeam;

        width--;
        // transpose();
        //
        // removeHorizontalSeam(seam);
        // transpose();
        // int[][] temprgb = new int[height()][width() - 1];
        // double[][] tempenergy = new double[height()][width() - 1];
        //
        // copyArrayWithoutSeam(temprgb, tempenergy, seam);
        // this.rgb = temprgb;
        // this.energy = tempenergy;
        // this.width--;
        //
        // recalculateEnergy(seam);
    }

    //  unit testing (required)
    public static void main(String[] args) {
        Picture picture = new Picture(args[0]);
        SeamCarver carver = new SeamCarver(picture);

        // StdOut.printf("Vertical seam: { ");
        // int[] verticalSeam = carver.findVerticalSeam();
        // for (int x : verticalSeam)
        //     StdOut.print(x + " ");
        // StdOut.println("}");

        // Used Print Energy.java to test energy values.

        Stopwatch timer = new Stopwatch();

        int[] verticalSeam = carver.findVerticalSeam();
        carver.removeVerticalSeam(verticalSeam);
        int[] horizontalSeam = carver.findHorizontalSeam();
        carver.removeHorizontalSeam(horizontalSeam);

        System.out.println(timer.elapsedTime());


    }

}

// Failed code


// private void traverse(int col, int row, double[][] energyArray, double[][] distTo,
//                       int[][] edgeTo) {
//
//     // if (col == width - 1) {
//     //     relaxEnd(row, col);
//     // }
//     // else {
//     relaxNotEnd(col, row, col, row + 1, energyArray, distTo, edgeTo);
//     if (col - 1 > 0) {
//         relaxNotEnd(col, row, col - 1, row + 1, energyArray, distTo, edgeTo);
//     }
//     if (col + 1 < width) {
//         relaxNotEnd(col, row, col + 1, row + 1, energyArray, distTo, edgeTo);
//     }
//     // }
//
//     // else {
//     //     if (i == height() - 1) {
//     //         relaxEnd(i, j);
//     //     }
//     //     else if (j == 0) {
//     //         relaxNotEnd(i, j, i + 1, j);
//     //         relaxNotEnd(i, j, i + 1, j + 1);
//     //     }
//     //     else if (j == width() - 1) {
//     //         relaxNotEnd(i, j, i + 1, j - 1);
//     //         relaxNotEnd(i, j, i + 1, j);
//     //
//     //     }
//     //     else {
//     //         relaxNotEnd(i, j, i + 1, j);
//     //         relaxNotEnd(i, j, i + 1, j - 1);
//     //         relaxNotEnd(i, j, i + 1, j + 1);
//     //     }
//     // }
//
// }

// private void copyArrayWithoutSeam(int[][] temprgb,
//          double[][] tempenergy, int[] seam) {
//     if (horizontal) {
//         for (int i = 0; i < width(); i++) {
//             int s = seam[i];
//             for (int j = 0; j < s; j++) {
//                 temprgb[j][i] = this.rgb[j][i];
//                 tempenergy[j][i] = this.energy[j][i];
//             }
//
//             for (int j = s + 1; j < height(); j++) {
//                 temprgb[j - 1][i] = this.rgb[j][i];
//                 tempenergy[j - 1][i] = this.energy[j][i];
//             }
//         }
//     }
//     else {
//         for (int i = 0; i < height(); i++) {
//             int s = seam[i];
//             for (int j = 0; j < s; j++) {
//                 temprgb[i][j] = this.rgb[i][j];
//                 tempenergy[i][j] = this.energy[i][j];
//             }
//
//             for (int j = s + 1; j < width(); j++) {
//                 temprgb[i][j - 1] = this.rgb[i][j];
//                 tempenergy[i][j - 1] = this.energy[i][j];
//             }
//         }
//     }
//
// }

// private void recalculateEnergy(int[] seam) {
//     if (horizontal) {
//         for (int col = 0; col < width(); col++) {
//             int s = seam[col];
//
//             if (s == 0) {
//                 this.energy[s][col] = pixelEnergy(col, s);
//             }
//             else if (s == height()) {
//                 this.energy[s - 1][col] = pixelEnergy(col, s - 1);
//             }
//             else {
//                 this.energy[s][col] = pixelEnergy(col, s);
//                 this.energy[s - 1][col] = pixelEnergy(col, s - 1);
//             }
//         }
//     }
//     else {
//         for (int row = 0; row < height(); row++) {
//             int s = seam[row];
//
//             if (s == 0) {
//                 this.energy[row][s] = pixelEnergy(s, row);
//             }
//             else if (s == width()) {
//                 this.energy[row][s - 1] = pixelEnergy(s - 1, row);
//             }
//             else {
//                 this.energy[row][s] = pixelEnergy(s, row);
//                 this.energy[row][s - 1] = pixelEnergy(s - 1, row);
//             }
//         }
//     }
// }


// Failed code for the energy. Reduced it and changed version being used.

// if (y == 0) {
//     up = new Color(rgb[height - 1][x]);
//     if (x == 0) {
//         left = new Color(rgb[y][width - 1]);
//         right = new Color(rgb[y][x + 1]);
//
//     }
//     else if (x == width - 1) {
//         left = new Color(rgb[y][x - 1]);
//         right = new Color(rgb[y][0]);
//
//     }
//     else {
//         left = new Color(rgb[y][x - 1]);
//         right = new Color(rgb[y][x + 1]);
//     }
//     down = new Color(rgb[y + 1][x]);
//
//
// }
// else if (y == height - 1) {
//     down = new Color(rgb[0][x]);
//     if (x == 0) {
//         left = new Color(rgb[y][width - 1]);
//         right = new Color(rgb[y][x + 1]);
//
//     }
//     else if (x == width - 1) {
//         left = new Color(rgb[y][x - 1]);
//         right = new Color(rgb[y][0]);
//
//     }
//     else {
//         left = new Color(rgb[y][x - 1]);
//         right = new Color(rgb[y][x + 1]);
//     }
//     up = new Color(rgb[y - 1][x]);
// }
// else if (x == 0) {
//     left = new Color(rgb[y][width - 1]);
//     right = new Color(rgb[y][x + 1]);
//     up = new Color(rgb[y - 1][x]);
//     down = new Color(rgb[y + 1][x]);
// }
// else if (x == width - 1) {
//     right = new Color(rgb[y][0]);
//     left = new Color(rgb[y][x - 1]);
//     up = new Color(rgb[y - 1][x]);
//     down = new Color(rgb[y + 1][x]);
// }
// else {
//     right = new Color(rgb[y][x + 1]);
//     left = new Color(rgb[y][x - 1]);
//     up = new Color(rgb[y - 1][x]);
//     down = new Color(rgb[y + 1][x]);
// }


// private void relaxEnd(int i, int j) {
//     if (distSink > distTo[i][j]) {
//         distSink = distTo[i][j];
//
//         edgeSink = i;
//
//         else {
//             edgeSink = j;
//         }
// }
// }


// failed code for the picture 
// Picture picture = new Picture(width(), height());
// for (int h = 0; h < height; h++) {
//     for (int w = 0; w < width; w++) {
//         picture.set(w, h, new Color(rgb[w][h]));
//     }
// }
