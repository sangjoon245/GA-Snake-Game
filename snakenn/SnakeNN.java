package snakenn;


import java.awt.Component;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JFrame;
import snakenn.Render;

public class SnakeNN {
  boolean endGame = false;
  
  boolean skipGen = false;
  
  int curX = 0;
  
  int curY = 0;
  
  int lastHighestValueIndex = -1;
  
  public final int snakeScale = 40;
  
  int foodX = (int)(Math.random() * 500.0D) * 40;
  
  int foodY = (int)(Math.random() * 400.0D) * 40;
  
  public Render render;
  
  public static SnakeNN snake;
  
  public JFrame jframe;
  
  public Toolkit toolkit;
  
  int numOfInput1 = 24;
  
  int numOfInput2 = 8;
  
  int numOfInput3 = 8;
  
  int numOfOutputs = 4;
  
  int numOfTopScores = 100;
  
  int numOfIndividuals = 2000;
  
  int speed = 40;
  
  int test = 0;
  
  double mutationrate = 0.04D;
  
  double crossrate = 0.3D;
  
  double totalscore = 0.0D;
  
  double[] inputs1 = new double[this.numOfInput1];
  
  double[] inputs2 = new double[this.numOfInput2];
  
  double[] inputs3 = new double[this.numOfInput3];
  
  double[] outputs = new double[this.numOfOutputs];
  
  double[] weights1 = new double[this.numOfInput1 * this.numOfInput2];
  
  double[] weights2 = new double[this.numOfInput2 * this.numOfInput3];
  
  double[] weights3 = new double[this.numOfInput3 * this.numOfOutputs];
  
  double[] parentsweight1 = new double[this.numOfInput1 * this.numOfInput2 + this.numOfInput2 * this.numOfInput3 + this.numOfInput3 * this.numOfOutputs];
  
  double[] parentsweight2 = new double[this.numOfInput1 * this.numOfInput2 + this.numOfInput2 * this.numOfInput3 + this.numOfInput3 * this.numOfOutputs];
  
  double[][] savedWeights = new double[this.numOfTopScores][this.numOfInput1 * this.numOfInput2 + this.numOfInput2 * this.numOfInput3 + this.numOfInput3 * this.numOfOutputs + 1];
  
  double[][] weightsData = new double[this.numOfIndividuals][this.numOfInput1 * this.numOfInput2 + this.numOfInput2 * this.numOfInput3 + this.numOfInput3 * this.numOfOutputs + 1];
  
  public KeyEvent keyevent;
  
  public double sigmoid(double x) {
    if (x < 0.0D)
      return -1.0D * (1.0D - Math.exp(x)); 
    if (x == 0.0D)
      return 0.0D; 
    return 1.0D - Math.exp(-x);
  }
  
  public SnakeNN() {
    int i;
    for (i = 0; i < this.numOfInput1 * this.numOfInput2; i++)
      this.weights1[i] = returnRandom(); 
    for (i = 0; i < this.numOfInput2 * this.numOfInput3; i++)
      this.weights2[i] = returnRandom(); 
    for (i = 0; i < this.numOfInput3 * this.numOfOutputs; i++)
      this.weights3[i] = returnRandom(); 
    Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
    this.jframe = new JFrame("Snake");
    this.jframe.setVisible(true);
    this.jframe.setResizable(false);
    this.jframe.setSize(1550, 825);
    this.jframe.add((Component)(this.render = new Render()));
    this.jframe.setLocation(dim.width / 2 - this.jframe.getWidth() / 2, dim.height / 2 - this.jframe.getHeight() / 2);
    this.jframe.setDefaultCloseOperation(3);
    this.jframe.addKeyListener((KeyListener)new Object(this));
    while (!this.endGame) {
      try {
        if (!this.skipGen) {
          Thread.sleep(this.speed);
        } else {
          Thread.sleep(1L);
        } 
      } catch (InterruptedException e) {}
      if (!this.render.alive) {
        int n;
        for (n = 0; n < this.numOfInput1 * this.numOfInput2; n++)
          this.weightsData[this.render.individual - 1][n] = this.weights1[n]; 
        for (n = 0; n < this.numOfInput2 * this.numOfInput3; n++)
          this.weightsData[this.render.individual - 1][n + this.numOfInput1 * this.numOfInput2] = this.weights2[n]; 
        for (n = 0; n < this.numOfInput3 * this.numOfOutputs; n++)
          this.weightsData[this.render.individual - 1][n + this.numOfInput1 * this.numOfInput2 + this.numOfInput2 * this.numOfInput3] = this.weights3[n]; 
        this.weightsData[this.render.individual - 1][this.numOfInput1 * this.numOfInput2 + this.numOfInput2 * this.numOfInput3 + this.numOfInput3 * this.numOfOutputs] = this.render.individualScore;
        if (this.render.individual == this.numOfIndividuals) {
          this.skipGen = false;
          this.render.individual = 1;
          this.render.gen++;
          for (n = 0; n < this.numOfTopScores; n++) {
            double tempHighestScore = 0.0D;
            int indexOfTempHighestScore = 0;
            for (int i1 = 0; i1 < this.numOfIndividuals; i1++) {
              if (this.weightsData[i1][this.numOfInput1 * this.numOfInput2 + this.numOfInput2 * this.numOfInput3 + this.numOfInput3 * this.numOfOutputs] > tempHighestScore) {
                tempHighestScore = this.weightsData[i1][this.numOfInput1 * this.numOfInput2 + this.numOfInput2 * this.numOfInput3 + this.numOfInput3 * this.numOfOutputs];
                indexOfTempHighestScore = i1;
              } 
            } 
            for (int j2 = 0; j2 < this.numOfInput1 * this.numOfInput2 + this.numOfInput2 * this.numOfInput3 + this.numOfInput3 * this.numOfOutputs + 1; j2++)
              this.savedWeights[n][j2] = this.weightsData[indexOfTempHighestScore][j2]; 
            this.weightsData[indexOfTempHighestScore][this.numOfInput1 * this.numOfInput2 + this.numOfInput2 * this.numOfInput3 + this.numOfInput3 * this.numOfOutputs] = 0.0D;
          } 
          this.totalscore = 0.0D;
          for (n = 0; n < this.numOfTopScores; n++) {
            if (this.savedWeights[n][this.numOfInput1 * this.numOfInput2 + this.numOfInput2 * this.numOfInput3 + this.numOfInput3 * this.numOfOutputs] >= 0.0D)
              this.totalscore += this.savedWeights[n][this.numOfInput1 * this.numOfInput2 + this.numOfInput2 * this.numOfInput3 + this.numOfInput3 * this.numOfOutputs]; 
          } 
        } 
        this.render.totalScore = this.totalscore;
      } 
      if (!this.render.alive && this.render.gen > 1) {
        double pointer = Math.abs(Math.random() * this.totalscore);
        this.render.snakepointer = pointer;
        int counter = 0;
        int lasti = 0;
        for (int n = 0; n < this.numOfTopScores; n++) {
          if (pointer > this.savedWeights[n][this.numOfInput1 * this.numOfInput2 + this.numOfInput2 * this.numOfInput3 + this.numOfInput3 * this.numOfOutputs]) {
            if (this.savedWeights[n][this.numOfInput1 * this.numOfInput2 + this.numOfInput2 * this.numOfInput3 + this.numOfInput3 * this.numOfOutputs] >= 0.0D)
              pointer -= this.savedWeights[n][this.numOfInput1 * this.numOfInput2 + this.numOfInput2 * this.numOfInput3 + this.numOfInput3 * this.numOfOutputs]; 
          } else if (counter == 0) {
            this.parentsweight1 = this.savedWeights[n];
            lasti = n;
            counter++;
            pointer = Math.abs(Math.random() * this.totalscore);
            n = 0;
          } else {
            if (n != lasti) {
              this.parentsweight2 = this.savedWeights[n];
              counter--;
              break;
            } 
            pointer = Math.abs(Math.random() * this.totalscore);
            n = 0;
          } 
        } 
        int crosscounter = 0;
        int i1;
        for (i1 = 0; i1 < this.numOfInput1 * this.numOfInput2; i1++) {
          double mutationscore = Math.abs(Math.random());
          double crossratescore = Math.abs(Math.random());
          if (crossratescore < this.crossrate)
            if (crosscounter == 0) {
              crosscounter = 1;
            } else {
              crosscounter = 0;
            }  
          if (counter == 0) {
            if (mutationscore < this.mutationrate) {
              this.weights1[i1] = returnRandom();
            } else {
              this.weights1[i1] = this.parentsweight1[i1];
            } 
          } else if (mutationscore < this.mutationrate) {
            this.weights1[i1] = returnRandom();
          } else {
            this.weights1[i1] = this.parentsweight2[i1];
          } 
        } 
        for (i1 = 0; i1 < this.numOfInput2 * this.numOfInput3; i1++) {
          double mutationscore = Math.abs(Math.random());
          double crossratescore = Math.abs(Math.random());
          if (crossratescore < this.crossrate)
            if (crosscounter == 0) {
              crosscounter = 1;
            } else {
              crosscounter = 0;
            }  
          if (counter == 0) {
            if (mutationscore < this.mutationrate) {
              this.weights2[i1] = returnRandom();
            } else {
              this.weights2[i1] = this.parentsweight1[i1 + this.numOfInput1 * this.numOfInput2];
            } 
          } else if (mutationscore < this.mutationrate) {
            this.weights2[i1] = returnRandom();
          } else {
            this.weights2[i1] = this.parentsweight2[i1 + this.numOfInput1 * this.numOfInput2];
          } 
        } 
        for (i1 = 0; i1 < this.numOfInput3 * this.numOfOutputs; i1++) {
          double mutationscore = Math.abs(Math.random());
          double crossratescore = Math.abs(Math.random());
          if (crossratescore < this.crossrate)
            if (crosscounter == 0) {
              crosscounter = 1;
            } else {
              crosscounter = 0;
            }  
          if (counter == 0) {
            if (mutationscore < this.mutationrate) {
              this.weights3[i1] = returnRandom();
            } else {
              this.weights3[i1] = this.parentsweight1[i1 + this.numOfInput1 * this.numOfInput2 + this.numOfInput2 * this.numOfInput3];
            } 
          } else if (mutationscore < this.mutationrate) {
            this.weights3[i1] = returnRandom();
          } else {
            this.weights3[i1] = this.parentsweight2[i1 + this.numOfInput1 * this.numOfInput2 + this.numOfInput2 * this.numOfInput3];
          } 
        } 
      } else if (!this.render.alive && this.render.gen == 1) {
        int n;
        for (n = 0; n < this.numOfInput1 * this.numOfInput2; n++)
          this.weights1[n] = returnRandom(); 
        for (n = 0; n < this.numOfInput2 * this.numOfInput3; n++)
          this.weights2[n] = returnRandom(); 
        for (n = 0; n < this.numOfInput3 * this.numOfOutputs; n++)
          this.weights3[n] = returnRandom(); 
      } 
      int k = 0;
      for (int j = 0; j < 8; j++) {
        this.inputs1[k] = this.render.spaceFood[j];
        k++;
        this.inputs1[k] = this.render.spaceBody[j];
        k++;
        this.inputs1[k] = this.render.spaceWall[j];
        k++;
      } 
      double inputholder = 0.0D;
      k = 0;
      int m = 0;
      for (k = 0; k < this.numOfInput1 * this.numOfInput2 + 1; k++) {
        if (k % this.numOfInput1 == 0 && k > 0) {
          this.inputs2[m] = sigmoid(inputholder);
          m++;
          inputholder = 0.0D;
        } 
        if (k != this.numOfInput1 * this.numOfInput2)
          inputholder += this.weights1[k] * this.inputs1[k % this.numOfInput1]; 
      } 
      m = 0;
      inputholder = 0.0D;
      for (k = 0; k < this.numOfInput2 * this.numOfInput3 + 1; k++) {
        if (k % this.numOfInput2 == 0 && k > 0) {
          this.inputs3[m] = sigmoid(inputholder);
          m++;
          inputholder = 0.0D;
        } 
        if (k != this.numOfInput2 * this.numOfInput3)
          inputholder += this.weights2[k] * this.inputs2[k % this.numOfInput2]; 
      } 
      m = 0;
      inputholder = 0.0D;
      for (k = 0; k < this.numOfInput3 * this.numOfOutputs + 1; k++) {
        if (k % this.numOfInput3 == 0 && k > 0) {
          this.outputs[m] = sigmoid(inputholder);
          m++;
          inputholder = 0.0D;
        } 
        if (k != this.numOfInput3 * this.numOfOutputs)
          inputholder += this.weights3[k] * this.inputs3[k % this.numOfInput3]; 
      } 
      this.render.test0 = this.inputs1[0];
      this.render.test1 = this.inputs1[3];
      this.render.test2 = this.inputs1[6];
      this.render.test3 = this.inputs1[9];
      this.render.test4 = this.inputs1[12];
      this.render.test5 = this.inputs1[15];
      this.render.test6 = this.inputs1[18];
      this.render.test7 = this.inputs1[21];
      int highestValueIndex = 0;
      double highestValue = 0.0D;
      for (k = 0; k < this.numOfOutputs; k++) {
        if (this.outputs[k] > highestValue) {
          highestValueIndex = k;
          highestValue = this.outputs[k];
        } 
      } 
      if (highestValueIndex == 0 && this.lastHighestValueIndex != 2) {
        this.curX = 0;
        this.curY = -40;
        this.lastHighestValueIndex = highestValueIndex;
      } else if (highestValueIndex == 1 && this.lastHighestValueIndex != 3) {
        this.curX = 40;
        this.curY = 0;
        this.lastHighestValueIndex = highestValueIndex;
      } else if (highestValueIndex == 2 && this.lastHighestValueIndex != 0) {
        this.curX = 0;
        this.curY = 40;
        this.lastHighestValueIndex = highestValueIndex;
      } else if (highestValueIndex == 3 && this.lastHighestValueIndex != 1) {
        this.curX = -40;
        this.curY = 0;
        this.lastHighestValueIndex = highestValueIndex;
      } 
      this.render.outputValues[0] = this.outputs[0];
      this.render.outputValues[1] = this.outputs[1];
      this.render.outputValues[2] = this.outputs[2];
      this.render.outputValues[3] = this.outputs[3];
      Update();
    } 
  }
  
  public void Update() {
    if (Math.abs(this.render.foodX - this.render.x) > Math.abs(this.render.foodX - this.render.x + this.curX)) {
      this.render.closer = true;
    } else if (Math.abs(this.render.foodX - this.render.x) < Math.abs(this.render.foodX - this.render.x + this.curX)) {
      this.render.closer = false;
    } 
    if (Math.abs(this.render.foodY - this.render.y) > Math.abs(this.render.foodY - this.render.y + this.curY)) {
      this.render.closer = true;
    } else if (Math.abs(this.render.foodY - this.render.y) < Math.abs(this.render.foodY - this.render.y + this.curY)) {
      this.render.closer = false;
    } 
    this.render.x += this.curX;
    this.render.y += this.curY;
    if (this.render.foodCollision == true) {
      this.render.increaseSnake();
      this.render.increaseSnake();
      this.render.increaseSnake();
      this.render.increaseSnake();
      this.render.increaseSnake();
      this.render.increaseSnake();
      this.render.foodCollision = false;
    } 
    this.render.repaint();
  }
  
  public static double returnRandom() {
    double temp = Math.random();
    if (temp < 0.5D) {
      temp = -1.0D;
    } else {
      temp = 1.0D;
    } 
    double value = Math.random();
    return temp * Math.floor(value * 100.0D) / 100.0D;
  }
  
  public static void main(String[] args) {
    snake = new SnakeNN();
  }
}
