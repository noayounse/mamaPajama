import simpleTween.*;

import camcam.*;
CamCam cam;

import mamaPajama.*;

PVector[] pts;
String[] test;
String a = "Lorem    ipsum  dolor sit amet, consectetur   adipiscing elit. Praesent venenatis sit amet nibh non congue. Nulla tristique molestie consectetur. Donec fringilla vel mi sit amet sagittis. Donec consectetur felis ac facilisis eleifend. Vestibulum sed porttitor diam. Curabitur nec urna at erat mollis facilisis ac scelerisque dolor. Proin consectetur ligula felis, sed rutrum felis commodo nec. Phasellus orci velit, pharetra ac laoreet sit amet, gravida at neque. Sed lobortis ut ligula nec consectetur. Suspendisse non convallis nisi, ut sagittis orci.";
PFont f;
float txtW = 400f;
int sz = 14;

void setup() {
  size(500, 500, P3D);
  cam = new CamCam(this);
  MamaPajama.begin(this);
  pts = new PVector[0];
  for (int i = 0; i < 8; i++) {
    pts = (PVector[])append(pts, new PVector(random(width), random(height)));
  }
  makeNewSplit();
} // end setup

void draw() {
  background(255);
  cam.useCamera();
  MamaPajama.drawGrid(200, 20);
  MamaPajama.drawOrigin();
  stroke(0);
  PVector ptA = new PVector(40, 40, 140);
  PVector ptB = new PVector(-50, -50, -50);
  PVector ptC = new PVector(-150, -150, -150);
  MamaPajama.drawPoint(ptA, 10);
  stroke(0, 255, 0);
  MamaPajama.drawSegment(ptC, ptB, 20, true);

  stroke(255, 0, 0);
  PVector rotatedPoint = MamaPajama.rotate3d(ptC, ptB, map(mouseX, 0, width, 0, 2 * TWO_PI), ptA);
  MamaPajama.drawPoint(rotatedPoint, 10);




  cam.pauseCamera();

  fill(255, 0, 0, 50);
  stroke(0);
  beginShape();
  for (PVector p : pts) {
    vertex(p.x, p.y);
  }
  endShape(CLOSE);
  PVector mouseLoc = new PVector(mouseX, mouseY);

  fill(255, 255, 0);
  if (MamaPajama.isInsidePolygon(mouseLoc, pts)) fill(255, 0, 0);
  ellipse(mouseLoc.x, mouseLoc.y, 5, 5);

  float xPos = 20;
  float otherXPos = xPos + txtW;
  float yPos = 20;
  textAlign(LEFT, TOP);
  stroke(0, 255, 255);
  line(xPos, 0, xPos, height);
  line(otherXPos, 0, otherXPos, height);
  noStroke();
  fill(0, 200);
  for (String s : test) {
    text(s, xPos, yPos);
    yPos += sz;
  }
} // end draw

void keyReleased() {
  if (key == ' ') MamaPajama.printStatus();
  else if (key == 't') makeNewSplit();
} // end keyReleased

void makeNewSplit() {
  sz = (int)random(4, 38);
  f = createFont("Helvetica", sz);
  test = MamaPajama.splitStringIntoLines(a, txtW, f);
} // end makeNewSplit

