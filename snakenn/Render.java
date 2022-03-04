package snakenn;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import javax.swing.JPanel;
import snakenn.SnakeNN;

public class Render extends JPanel {
  boolean DEBUG = true;
  
  boolean skipGen = false;
  
  double test0;
  
  double test1;
  
  double test2;
  
  double test3;
  
  double test4;
  
  double test5;
  
  double test6;
  
  double test7 = 0.0D;
  
  double[] spaceFood = new double[8];
  
  double[] spaceWall = new double[8];
  
  double[] spaceBody = new double[8];
  
  final int snakeScale = 40;
  
  int snakeStartX = 480;
  
  int snakeStartY = 400;
  
  int x = 480;
  
  int y = 400;
  
  double[] outputValues = new double[4];
  
  Node head = new Node(this);
  
  Node tail = this.head;
  
  int snakeLength = 1;
  
  int highScore = 1;
  
  int gen = 1;
  
  int individual = 1;
  
  int moves = 0;
  
  double moves2 = 0.0D;
  
  int currentMoves = 0;
  
  int score = 0;
  
  double distanceFromFood = 0.0D;
  
  double negative = 0.0D;
  
  int test = 0;
  
  double snakepointer = 0.0D;
  
  double totalScore = 0.0D;
  
  double individualScore = 0.0D;
  
  int foodX = (int)(Math.random() * 25.0D) * 40;
  
  int foodY = (int)(Math.random() * 18.0D) * 40;
  
  int deadByWall = 0;
  
  boolean foodCollision = false;
  
  boolean restart = true;
  
  boolean alive = true;
  
  boolean closer = false;
  
  public double truncate(double a) {
    return Math.floor(a * 100.0D) / 100.0D;
  }
  
  public void increaseSnake() {
    this.snakeLength++;
    Node temp = this.head;
    while (temp.previous != null)
      temp = temp.previous; 
    Node newNode = new Node(this);
    newNode.x = temp.x;
    newNode.y = temp.y;
    temp.previous = newNode;
    this.tail = newNode;
    this.tail.next = temp;
  }
  
  public double returnScore(double move2, double move, double score, double negative) {
    if (this.moves2 < 25.0D)
      return Math.pow(1.2D, this.moves2 / 1.5D) + 2000.0D * score * score; 
    return Math.pow(1.2D, 16.0D) + (this.moves2 - 24.0D) * 2.0D + 2000.0D * score * score;
  }
  
  public double sigmoid(double x) {
    if (x < 0.0D)
      return 1.0D - Math.exp(x); 
    if (x == 0.0D)
      return 0.0D; 
    return 1.0D - Math.exp(-x);
  }
  
  public int returnPositionX(int index) {
    Node temp = this.head;
    for (int i = 0; i < index; i++)
      temp = temp.previous; 
    return temp.x;
  }
  
  public int returnPositionY(int index) {
    Node temp = this.head;
    for (int i = 0; i < index; i++)
      temp = temp.previous; 
    return temp.y;
  }
  
  public boolean touchingBody(int x, int y) {
    Node temp = this.tail;
    for (int i = 0; i < this.snakeLength - 2; i++) {
      if (temp.x == x && temp.y == y)
        return true; 
      temp = temp.next;
    } 
    return false;
  }
  
  public boolean touchingBodyLine(int x, int y) {
    Node temp = this.tail;
    for (int i = 0; i < this.snakeLength - 2; i++) {
      if (temp.x == x || temp.y == y)
        return true; 
      temp = temp.next;
    } 
    return false;
  }
  
  public boolean touchingWall(int x, int y) {
    if (x >= 1000 || y >= 780 || x <= 0 || y <= 0)
      return true; 
    return false;
  }
  
  public boolean touchingFood(int x, int y) {
    if (x == this.foodX && y == this.foodY)
      return true; 
    return false;
  }
  
  public void paintComponent(Graphics g) {
    this.negative = 0.0D;
    if (this.alive == true) {
      this.negative = 0.0D;
      if (this.test == 0) {
        increaseSnake();
        increaseSnake();
        increaseSnake();
        increaseSnake();
        this.test = 1;
      } 
      Node temp = this.tail;
      int i;
      for (i = 0; i < this.snakeLength - 1; i++) {
        temp.x = temp.next.x;
        temp.y = temp.next.y;
        temp = temp.next;
      } 
      this.head.x = this.x;
      this.head.y = this.y;
      super.paintComponent(g);
      g.setColor(Color.black);
      g.fillRect(0, 0, 1040, 860);
      g.setColor(Color.red);
      g.fillRect(this.foodX, this.foodY, 40, 40);
      g.setColor(Color.yellow);
      for (i = 0; i < this.snakeLength; i++)
        g.fillRect(returnPositionX(i), returnPositionY(i), 40, 40); 
      if (this.head.x == this.foodX && this.head.y == this.foodY) {
        int j = (int)(Math.random() * 25.0D) * 40;
        int k = (int)(Math.random() * 18.0D) * 40;
        while (touchingBody(j, k) == true) {
          j = (int)(Math.random() * 25.0D) * 40;
          k = (int)(Math.random() * 18.0D) * 40;
        } 
        this.score++;
        this.currentMoves = 0;
        this.foodX = j;
        this.foodY = k;
        this.foodCollision = true;
      } 
      int tempx = this.x;
      int tempy = this.y;
      double counter = 0.0D;
      if (touchingBody(this.x, this.y))
        this.alive = false; 
      if (touchingWall(this.x, this.y))
        this.alive = false; 
      if (this.currentMoves > 95)
        this.alive = false; 
      this.individualScore = returnScore(this.moves2, this.moves, this.score, this.negative);
      this.spaceFood[0] = 0.0D;
      this.spaceBody[0] = 0.0D;
      this.spaceWall[0] = 0.0D;
      while (true) {
        tempy -= 40;
        if (touchingFood(tempx, tempy))
          this.spaceFood[0] = counter / 20.0D; 
        if (touchingBody(tempx, tempy))
          this.spaceBody[0] = counter / 20.0D; 
        if (touchingWall(tempx, tempy)) {
          this.spaceWall[0] = counter / 20.0D;
          break;
        } 
        counter++;
      } 
      counter = 0.0D;
      tempx = this.x;
      tempy = this.y;
      this.spaceFood[1] = 0.0D;
      this.spaceBody[1] = 0.0D;
      this.spaceWall[1] = 0.0D;
      while (true) {
        tempy -= 40;
        tempx += 40;
        if (touchingFood(tempx, tempy))
          this.spaceFood[1] = counter / 20.0D; 
        if (touchingBody(tempx, tempy))
          this.spaceBody[1] = counter / 20.0D; 
        if (touchingWall(tempx, tempy)) {
          this.spaceWall[1] = counter / 20.0D;
          break;
        } 
        counter++;
      } 
      counter = 0.0D;
      tempx = this.x;
      tempy = this.y;
      this.spaceFood[2] = 0.0D;
      this.spaceBody[2] = 0.0D;
      this.spaceWall[2] = 0.0D;
      while (true) {
        tempx += 40;
        if (touchingFood(tempx, tempy))
          this.spaceFood[2] = counter / 27.0D; 
        if (touchingBody(tempx, tempy))
          this.spaceBody[2] = counter / 27.0D; 
        if (touchingWall(tempx, tempy)) {
          this.spaceWall[2] = counter / 27.0D;
          break;
        } 
        counter++;
      } 
      counter = 0.0D;
      tempx = this.x;
      tempy = this.y;
      this.spaceFood[3] = 0.0D;
      this.spaceBody[3] = 0.0D;
      this.spaceWall[3] = 0.0D;
      while (true) {
        tempx += 40;
        tempy += 40;
        if (touchingFood(tempx, tempy))
          this.spaceFood[3] = counter / 20.0D; 
        if (touchingBody(tempx, tempy))
          this.spaceBody[3] = counter / 20.0D; 
        if (touchingWall(tempx, tempy)) {
          this.spaceWall[3] = counter / 20.0D;
          break;
        } 
        counter++;
      } 
      counter = 0.0D;
      tempx = this.x;
      tempy = this.y;
      this.spaceFood[4] = 0.0D;
      this.spaceBody[4] = 0.0D;
      this.spaceWall[4] = 0.0D;
      while (true) {
        tempy += 40;
        if (touchingFood(tempx, tempy))
          this.spaceFood[4] = counter / 20.0D; 
        if (touchingBody(tempx, tempy))
          this.spaceBody[4] = counter / 20.0D; 
        if (touchingWall(tempx, tempy)) {
          this.spaceWall[4] = counter / 20.0D;
          break;
        } 
        counter++;
      } 
      counter = 0.0D;
      tempx = this.x;
      tempy = this.y;
      this.spaceFood[5] = 0.0D;
      this.spaceBody[5] = 0.0D;
      this.spaceWall[5] = 0.0D;
      while (true) {
        tempy += 40;
        tempx -= 40;
        if (touchingFood(tempx, tempy))
          this.spaceFood[5] = counter / 20.0D; 
        if (touchingBody(tempx, tempy))
          this.spaceBody[5] = counter / 20.0D; 
        if (touchingWall(tempx, tempy)) {
          this.spaceWall[5] = counter / 20.0D;
          break;
        } 
        counter++;
      } 
      counter = 0.0D;
      tempx = this.x;
      tempy = this.y;
      this.spaceFood[6] = 0.0D;
      this.spaceBody[6] = 0.0D;
      this.spaceWall[6] = 0.0D;
      while (true) {
        tempx -= 40;
        if (touchingFood(tempx, tempy))
          this.spaceFood[6] = counter / 27.0D; 
        if (touchingBody(tempx, tempy))
          this.spaceBody[6] = counter / 27.0D; 
        if (touchingWall(tempx, tempy)) {
          this.spaceWall[6] = counter / 27.0D;
          break;
        } 
        counter++;
      } 
      counter = 0.0D;
      tempx = this.x;
      tempy = this.y;
      this.spaceFood[7] = 0.0D;
      this.spaceBody[7] = 0.0D;
      this.spaceWall[7] = 0.0D;
      while (true) {
        tempx -= 40;
        tempy -= 40;
        if (touchingFood(tempx, tempy))
          this.spaceFood[7] = counter / 20.0D; 
        if (touchingBody(tempx, tempy))
          this.spaceBody[7] = counter / 20.0D; 
        if (touchingWall(tempx, tempy)) {
          this.spaceWall[7] = counter / 20.0D;
          break;
        } 
        counter++;
      } 
      this.moves++;
      this.currentMoves++;
      if (this.closer == true) {
        this.moves2++;
      } else {
        this.moves2++;
      } 
      int fontSize = 20;
      Font f = new Font("Andale Mono", 0, fontSize);
      g.setColor(Color.black);
      g.setFont(f);
      g.drawString("Score : " + (this.snakeLength / 7), 1050, 30);
      if (this.snakeLength > this.highScore)
        this.highScore = this.snakeLength; 
      g.drawString("Highscore : " + ((this.highScore - 1) / 6), 1050, 55);
      g.drawString("Gen : " + this.gen, 1050, 80);
      g.drawString("Individual : " + this.individual + "/2000", 1050, 105);
      double tempscore = returnScore(this.moves2, this.moves, this.score, this.negative);
      g.drawString("Current calculated Score : " + tempscore, 1050, 130);
      g.drawString("Current Outputs : ", 1050, 155);
      g.drawString("pointer :" + this.snakepointer, 1050, 305);
      g.drawString("Total score from last gen : " + this.totalScore, 1050, 330);
      if (this.DEBUG) {
        g.drawString(this.x + ", " + this.y, 1050, 760);
        g.drawString("[" + this.spaceFood[0] + ", " + this.spaceFood[2] + ", " + this.spaceFood[4] + ", " + this.spaceFood[6] + "]", 1050, 700);
        g.drawString("[" + this.spaceFood[1] + ", " + this.spaceFood[3] + ", " + this.spaceFood[5] + ", " + this.spaceFood[7] + "]", 1050, 730);
        g.drawString("[" + truncate(this.test0) + ", " + truncate(this.test1) + ", " + truncate(this.test2) + ", " + truncate(this.test3) + ", " + truncate(this.test4) + ", " + truncate(this.test5) + ", " + 
            truncate(this.test6) + ", " + truncate(this.test7), 1050, 670);
        g.drawString("up :" + this.outputValues[0], 1050, 180);
        g.drawString("down :" + this.outputValues[2], 1050, 205);
        g.drawString("right :" + this.outputValues[1], 1050, 230);
        g.drawString("left :" + this.outputValues[3], 1050, 255);
      } 
    } else {
      this.test = 0;
      if (this.restart == true && this.individual != 2000) {
        if (this.individual == 1999)
          this.skipGen = false; 
        this.score = 0;
        this.moves = 0;
        this.moves2 = 0.0D;
        this.currentMoves = 0;
        this.head.previous = null;
        this.tail = this.head;
        this.x = this.snakeStartX;
        this.y = this.snakeStartY;
        this.snakeLength = 1;
        this.alive = true;
        this.individual++;
        int tempx = (int)(Math.random() * 25.0D) * 40;
        int tempy = (int)(Math.random() * 18.0D) * 40;
        while (tempx == 480 || tempx == 440 || tempx == 520 || tempy == 400 || tempy == 360 || tempy == 440) {
          tempx = (int)(Math.random() * 25.0D) * 40;
          tempy = (int)(Math.random() * 18.0D) * 40;
        } 
        this.foodX = tempx;
        this.foodY = tempy;
      } 
    } 
  }
}
