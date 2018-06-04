The MAX-SAT problem is a generalization of the Boolean Satisfiability (SAT) problem. Given a Conjunctive Normal Form (CNF) of boolean variables, the aim is to find a boolean assignment to the variables in such a way, that maximum number of clauses are satisfied. There exists no polynomial time algorithm for MAX-SAT. Some existing algorithms use brute force technique, but they have exponential worst case time complexity. Randomization algorithms exist, but many suffer from local minima. This project uses the Seesaw Search (https://www.cs.rit.edu/~ark/mastersprojects/seesawsearch.shtml) technique for solving MAX-SAT, which is designed to work on multiple cores of a CPU, utilizing their full potential, to improve the efficiency of the search.

Include the Parallel Java 2 (PJ2) library (https://www.cs.rit.edu/~ark/pj2_20180501.jar) in your path.

The input files are provided in the 'input' directory.

Sequential Version of Seesaw Search:
There are 2 versions of the sequential Seesaw Search

After compiling all the files, run the following command for running Count based Seesaw Search:

java MAXSATSolverSeq_1 input-file max-tries

<input-file> = The .CNF file containing info about variables, and clauses.
<max-tries> = number of times to run the seesaw search, after which it will give up, and give whatever results were obtained. 

Example: java MAXSATSolverSeq_2 250-1065.cnf 100000

For running the Incremental Seesaw Search, run the follwing command:

java MAXSATSolverSeq_1 input-file max-tries

<input-file> = The .CNF file containing info about variables, and clauses.
<max-tries> = number of times to run the seesaw search, after which it will give up, and give whatever results were obtained.

Example: java MAXSATSolverSeq_2 250-1065.cnf 100000

/////////////////////////////////////////////////////////////////////

Parallel Version of Seesaw Search:
There are 3 versions of the parallel Seesaw Search

For running the Massively Parallel Count based Seesaw Search, run the following command:

java pj2 MAXSATSolverSmp_1 input-file max-tries seesawSearchInstances

<input-file> = The .CNF file containing info about variables, and clauses.
<max-tries> = number of times to run the seesaw search, after which it will give up, and give whatever results were obtained.
<seesawSearchInstances> = The number of Seesaw Search instances to run in parallel.

Example: java pj2 cores=50 MAXSATSolverSmp_1 250-1065.cnf 10000 100

For running the Massively Parallel Incremental Seesaw Search, run the follwing command:

java pj2 MAXSATSolverSmp_2 input-file max-tries seesawSearchInstances

<input-file> = The .CNF file containing info about variables, and clauses.
<max-tries> = number of times to run the seesaw search, after which it will give up, and give whatever results were obtained.
<seesawSearchInstances> = The number of Seesaw Search instances to run in parallel.

Example: java pj2 cores=50 MAXSATSolverSmp_2 250-1065.cnf 10000 100

For running the Massively Parallel Incremental Seesaw Search with restarts, run the follwing command:

java pj2 MAXSATSolverSmp_3 input-file max-tries seesawSearchInstances

<input-file> = The .CNF file containing info about variables, and clauses.
<max-tries> = number of times to run the seesaw search, after which it will give up, and give whatever results were obtained.
<seesawSearchInstances> = The number of Seesaw Search instances to run in parallel.

Example: java pj2 cores=50 MAXSATSolverSmp_3 250-1065.cnf 10000 100

