# GamesWithML

A project by the University of West Florida's (UWF) Artificial Intelligence (AI) Research Group (AIRG), GamesWithML combines the growing field of Neural Networks (NNs) with a suite of classic games, including Tic-Tac-Toe and Othello.

## Packages

GamesWithML contains the following packages under `io.github.uwfai`:

* `neural`: general-purpose NN framework
* `tictactoe`: TTT with GUI
* `othello`: Othello with GUI
* `controller`: combined menu GUI

## Neural Network Implementation

NNs are implemented using standard Stochastic Gradient Descent (SGD) and currently includes functionality for the following:

### `NeuralNetwork.CostType` (cost functions):
* `QUADRATIC`: ![C equals one over two times N all times the sum across X of Y sub X minus A sub X squared](http://latex.codecogs.com/gif.latex?C%3D%5Cfrac%7B1%7D%7B2n%7D%5Csum_%7Bx%7D%20%28y_%7Bx%7D-a_%7Bx%7D%29%5E%7B2%7D)
* `CROSSENTROPY`: ![C equals one over N times the sum across X of Y sub X times natural log of A sub X plus one minus Y sub X times natural log of one minus A sub X](http://latex.codecogs.com/gif.latex?C%3D%5Cfrac%7B1%7D%7Bn%7D%5Csum_%7Bx%7D%20%28y_%7Bx%7D%5C%20ln%5C%20a_%7Bx%7D&plus;%281-y_%7Bx%7D%29ln%281-a_%7Bx%7D%29%29)

### `NeuralNetwork.ActivationType` (activation functions):
* `SIGMOID`: ![sigma of z equals one over one minus E raised to negative Z](http://latex.codecogs.com/gif.latex?%5Csigma%20%28z%29%3D%5Cfrac%7B1%7D%7B1&plus;e%5E%7B-z%7D%7D)
* `RELU`: ![sigma of z equals natural log of one plus E raised to Z](http://latex.codecogs.com/gif.latex?%5Csigma%20%28z%29%3Dln%281&plus;e%5E%7Bz%7D%29)

### `NeuralNetwork.InitializeType` (initialization functions):
* `DUMB`: ![W sub X equals random minus random](http://latex.codecogs.com/gif.latex?w_%7Bx%7D%3Drand%28%29-rand%28%29), ![B sub X equals random minus random](http://latex.codecogs.com/gif.latex?b_%7Bx%7D%3Drand%28%29-rand%28%29)
* `SMART`: ![W sub X equals random minus random all over square root of n](http://latex.codecogs.com/gif.latex?w_%7Bx%7D%3D%5Cfrac%7Brand%28%29-rand%28%29%7D%7B%5Csqrt%7Bn%7D%7D), ![B sub X equals random minus random](http://latex.codecogs.com/gif.latex?b_%7Bx%7D%3Drand%28%29-rand%28%29)
