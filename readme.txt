Programming Assignment 7: Seam Carving


/* *****************************************************************************
 *  Describe concisely your algorithm to find a horizontal (or vertical)
 *  seam.
 **************************************************************************** */
My algorithm to find a vertical seam was to first start my search by making
my distTo array all infinite values except for the first row. I would set those values
up to be the same as my energy values which I would compute too in this method.

After this, I go through each pixel and relax it accordingly by looking at its
neighbors and computing if its neighbors new distTo's are less than what is
already there.

After this, I look at the last row and compute the minimum distTo value there, and
then I work my way back to the start by populating the seam.

In order to find the horizontal seam, I transpose the Picture and compute the
vertical seam on the transposed image. I then transpose it back after and return
the seam.



/* *****************************************************************************
 *  Describe what makes an image suitable to the seam-carving approach
 *  (in terms of preserving the content and structure of the original
 *  image, without introducing visual artifacts). Describe an image that
 *  would not work well.
 **************************************************************************** */
I use the method assert seam where it considers if it's removing a horizontal or
vertical seam with a boolean global variable.

I see whether the seam length meets the respective width or height criteria and
if no value in the seam is outside this criteria as well.

Afterwards, I check if the values in the seam are all valid by seeing if they
are all neighbors of each other.

An image suitable to the seam-carving approach would be if the seam contains
values that are all neighbors. One that wouldn't work well is one where the seam
values jump from 1 to 4 which are clearly not neighbors.

/* *****************************************************************************
 *  Perform computational experiments to estimate the running time to reduce
 *  a W-by-H image by one column and one row (i.e., one call each to
 *  findVerticalSeam(), removeVerticalSeam(), findHorizontalSeam(), and
 *  removeHorizontalSeam()). Use a "doubling" hypothesis, where you
 *  successively increase either W or H by a constant multiplicative
 *  factor (not necessarily 2).
 *
 *  To do so, fill in the two tables below. Each table must have 5-10
 *  data points, ranging in time from around 0.25 seconds for the smallest
 *  data point to around 30 seconds for the largest one.
 **************************************************************************** */

(keep W constant)
 W = 2000
 multiplicative factor (for H) = 2

 H           time (seconds)      ratio       log ratio
------------------------------------------------------
250          0.621              ----         ----
500          0.911              1.467       .5528
1000         1.764              1.936       .953
2000         2.451              1.389       .474
4000         4.583              1.870       .903
8000         11.833             2.582       1.368

Seem to convering around 1.

(keep H constant)
 H = 2000
 multiplicative factor (for W) = 2

 W           time (seconds)      ratio       log ratio
------------------------------------------------------
250         0.577               -----        -----
500         0.892               1.546       .6285
1000        1.182               1.325       .406
2000        2.803               2.3714      1.246
4000        4.664               1.664       .7347
8000        11.941              2.560       1.356

Seem to convering around 1.

/* *****************************************************************************
 *  Using the empirical data from the above two tables, give a formula
 *  (using tilde notation) for the running time (in seconds) as a function
 *  of both W and H, such as
 *
 *       ~ 5.3*10^-8 * W^5.1 * H^1.5
 *
 *  Briefly explain how you determined the formula for the running time.
 *  Recall that with tilde notation, you include both the coefficient
 *  and exponents of the leading term (but not lower-order terms).
 *  Round each coefficient and exponent to two significant digits.
 **************************************************************************** */


Running time (in seconds) to find and remove one horizontal seam and one
vertical seam, as a function of both W and H:


    ~  1.718 * 10^-6 * W^1 * H^1
       _______________________________________




/* *****************************************************************************
 *  Known bugs / limitations.
 **************************************************************************** */

Since we are using a 2d array for distTo and edgeTo, as the image gets bigger,
the running time gets bigger and bigger.

/* *****************************************************************************
 *  Describe whatever help (if any) that you received.
 *  Don't include readings, lectures, and precepts, but do
 *  include any help from people (including course staff, lab TAs,
 *  classmates, and friends) and attribute them by name.
 **************************************************************************** */

Nina- transpose, findVerticalSeam


/* *****************************************************************************
 *  Describe any serious problems you encountered.
 **************************************************************************** */
No serious problems encountered. Just a lot of debugging and testing. Lot of
failed attempts and failed code.

/* *****************************************************************************
 *  If you worked with a partner, assert below that you followed
 *  the protocol as described on the assignment page. Give one
 *  sentence explaining what each of you contributed.
 **************************************************************************** */
N/A

/* *****************************************************************************
 *  List any other comments here. Feel free to provide any feedback
 *  on how much you learned from doing the assignment, and whether
 *  you enjoyed doing it.
 **************************************************************************** */

Failed code is below the main method. I realized after a long time of debugging
that finding the vertical seam first is better specifically for the methods
in the picture class and the energy classes as it was expecting x as the column
and y as the row. After discovering this, computing the functions was pretty easy
and straightforward. Finding the energies was pretty staightforward as the
checklist section helped with this.

Wanted to show work so failed code or past code is in comments as well throughout
my code.
