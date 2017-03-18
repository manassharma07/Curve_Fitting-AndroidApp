# Curve_Fitting-AndroidApp
Android App to find the best fit for a given set of data.

Welcome to Curve Fit - Tools

<a href="http://www.bragitoff.com/wp-content/uploads/2017/02/FIT.png"><img class="aligncenter wp-image-4243 size-full" src="http://www.bragitoff.com/wp-content/uploads/2017/02/FIT.png" width="1024" height="500" /></a>

Curve Fit - Tools helps you find out the best fit to a curve using the Least Squares Approximation Method.

You can find an exponential, linear or a polynomial fit for any curve.

Curve Fit- Tools is better than most of the apps in the Play Store that let you do the same things for a variety of reasons.
<ol>
 	<li>With Curve Fit you can virtually input unlimited amount of data.</li>
 	<li>You can use potentially any degree polynomial you want.</li>
 	<li>You can save your data to a csv file which can later on be opened by MS Excel or any other software of your choice.</li>
 	<li>The graphs are very neat and zoomable.</li>
 	<li>The algorithms are fast and ensure quick processing.</li>
 	<li>More features will be added even though it already out performs most other apps.</li>
</ol>
The results of the app were cross checked with a lot of existing softwares for a variety of data sets and the results were uniform.

<a href="http://www.bragitoff.com/wp-content/uploads/2017/02/curve.png"><img class="alignright size-medium wp-image-4244" src="http://www.bragitoff.com/wp-content/uploads/2017/02/curve-300x300.png" alt="" width="300" height="300" /></a>

<strong>Download Link: </strong><a href="https://play.google.com/store/apps/details?id=com.bragitoff.curvefit_leastsquares">https://play.google.com/store/apps/details?id=com.bragitoff.curvefit_leastsquares</a>
<h2>Help:</h2>
<h3>Entering the data-set:</h3>
<ul>
 	<li>Enter the x-values and y-values using the form given and press submit after each entry to store it.</li>
 	<li>You can scroll through the list of data points to see if you entered them correctly.
Note: You can enter a new data-set or delete the existing values by clicking on 'Clear' button.</li>
 	<li>You need to provide at least two pairs of xy values and equal number of x and y values.</li>
 	<li>Also you cannot repeat an x-value.</li>
 	<li>Exponential Fit cannot be calculated for negative y-values and will result in NaN values or, highly unlikely, a crash.</li>
 	<li>A quick hack---&gt; Instead of clicking on the 'Done' key after entering a data-point, click on the 'Submit' button. Pressing the 'Done' takes away the keyboard and you have to click on the form again and again thus slowing down the process. Pressing the 'Submit' key doesn't take away the keyboard and will improve the data entering speed.</li>
</ul>
<h3>Calculating the Fit:</h3>
After entering your data-set just click on 'Calculate' and on the next screen choose from three options: (i) Exponential Fitting, (ii) Linear Fitting and (iii) Polynomial Fitting.

If you choose 'Polynomial Fit', enter the degree of polynomial that you want to fit the curve with.
Then click on 'Submit'.
<h3>Saving Data as CSV:</h3>
After entering your data-set you can save it as a csv file for future reference.

Files are saved in a folder called 'Curve Fitter CSV', which would be created either inside your external storage or internal storage.

<strong>What is a CSV?</strong> - <a href="https://en.wikipedia.org/wiki/Comma-separated_values">Wikipedia </a>
CSV files can be read by any excel type software or if you have an Office Suite on your phone.
You can also transfer the file to your computer and open it on MS Excel or any other software of your preference.
<h3>Reading Data from a CSV:</h3>
Curve Fit-Tools has a unique feature which lets you read CSV files from your storage.
This feature is extremely useful and desirable as it allows you to re-analyze a data-set that you saved as a CSV before, thus, saving you a lot of time.

You could also have someone mail you their data as a csv and you could analyze it on the go.

To read a CSV: Just click on 'Read CSV' button on the Main Screen. This would bring up a file browser showing the contents of your external directory, you could then browse and select your CSV file. The data would be read and the x-y data would be filled out automatically.

Note: Your CSV file should contain the x-axis values in the first column and the y-axis values in the second column.
The app uses it's own built-in file browser and not the one installed on your device.
<h2>Algorithm:</h2>
Curve Fit- Tools calculates the fit using the Least Square Approximation Method(<a href="https://en.wikipedia.org/wiki/Least_squares">Wikipedia</a>).

Linear Fitting: <a href="http://www.bragitoff.com/2015/10/linear-fit-lab-write-up-with-algorithm-and-flow-chart/">Algorithm</a>

Exponential Fitting: <a href="http://www.bragitoff.com/2015/10/exponential-fit-lab-write-up-with-algorithm-and-flow-chart/">Algorithm</a>

Polynomial Fitting: <a href="http://www.bragitoff.com/2015/10/polynomial-fit-lab-write-up-with-algorithm-and-flowchart/">Algorithm</a>

More useful resources on Numerical Methods can be found on my(developer) blog: <a href="http://bragitoff.com">bragitoff.com</a> under the category -&gt;Physics&gt;Numerical Analysis.
<h2>Graphing Library:</h2>
Curve Fit-Tools uses the <a href="http://www.android-graphview.org">GraphView library</a> by <a href="https://plus.google.com/+JonasGehring">jjoe</a>.
<div class="ipWidget ipWidget-Heading ipSkin-default">
<h4 class="_title">License</h4>
</div>
<div class="ipWidget ipWidget-Text ipSkin-default">
<div class="ipsContainer">

GraphView is currently published under Apache v2 license.

Which means that you <b>are allowed</b> to use it in your closed source applications.
See the complete license here: <a href="https://github.com/jjoe64/GraphView/blob/master/license.txt" target="_blank">https://github.com/jjoe64/GraphView/blob/master/license.txt
</a>And more information about that license model at wikipedia: <a href="https://en.wikipedia.org/wiki/Apache_License" target="_blank">https://en.wikipedia.org/wiki/Apache_License</a>

</div>
</div>
<h2>CSV Read/Write Library</h2>
Curve Fit-Tools uses the <a href="http://opencsv.sourceforge.net/">opencsv</a> library by <a href="http://blogs.bytecode.com.au/glen/">Glen Smith</a> for reading and writing CSV's.
<h4>License</h4>
opencsv is published under Apache v2 license.

To find out more details of the license, read the <a class="externalLink" href="http://www.apache.org/licenses/LICENSE-2.0">Apache 2.0 license agreement</a>.
<h2>File Picking Library</h2>
Curve Fit-Tools uses the <a href="https://github.com/nbsp-team/MaterialFilePicker">MaterialFilePicker </a>library by <a href="https://github.com/nbsp-team">nbsp-Team</a> for picking up the CSV file from directory.
<h4>License:</h4>
MaterialFilePicker is published under Apache v2 <a href="https://github.com/nbsp-team/MaterialFilePicker/blob/master/LICENSE">License. </a>

To find out more details of the license, read the <a class="externalLink" href="http://www.apache.org/licenses/LICENSE-2.0">Apache 2.0 license agreement</a>.
