This projecct simulates the behviour of the microprocessor pipeline.
Sample instruction files are provided in input files. 
Overall structure of code organization and responsibility of modules is explained in designed document.
How to run the project in explained below.

1) Run Make clean to remove all class file from the current folder.
2) Run Make in linux environment to build program. It will generate .class files in current directory.

3) Use 'java Simulator <path_to_input_file>' to run program. 
   If no filename is specified it will by default take input.txt as file name which must be available in current directory, otherwise it will say file not found while simulating.

4) type 'initialize' to initialize pipeline.
   type 'simulate n' : to simulate n number of cycles. e.g. 'simulate 10' which simulates 10 cycles.
   type 'display'   : to see contents of stages, register values and its valid bits also values of first 100 memory locations on screen.

5) design document is available in folder design_document.

6) Sample input files arer in input_files folder.

7) Running class files has been copied to executable_class_files folder . These are the same class files which are generaed by make in current directory.  
   'Simulator' is the class file which contains main method and runs simulator. Syntax to run is available in step 3.

8) Note that all the instructions available in input file must be in uppercase.
