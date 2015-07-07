import ocrUtils.maths.*;
import ocrUtils.*;
import ocrUtils.ocr3D.*;

color a;
PImage tonal;

ArrayList<Colors> myColors = new ArrayList();

void setup() {
  OCRUtils.begin(this);

  size(600, 500);
  smooth(15);
  colorMode(RGB, 255);



  pushStyle();
  colorMode(HSB, 360);
  PGraphics pg = createGraphics(height, height);
  pg.beginDraw();
  pg.background(0);

  PVector pt = new PVector();
  PVector center = new PVector(pg.width/2, pg.height/2);
  float maxDist = pg.height / 2;
  float dist;
  for (int y = 0; y < pg.height; y++) {
    for (int x = 0; x < pg.width; x++) {
      float angle = atan2(y - pg.height/2, x - pg.width/2);
      pt.set(x, y);
      dist = pt.dist(center);
      if (dist <= maxDist) pg.set(x, y, color(map(angle, -PI, PI, 0, 360), map(dist, 0, maxDist, 0, 360), 360));
    }
  }
  pg.endDraw();
  popStyle();
  tonal = (PImage)pg;

  loadColors();
} // end setup

//
void draw() {
  background(0);
  image(tonal, 0, 0);

  if (mousePressed) a = tonal.get(mouseX, mouseY);
  noStroke();
  fill(a);
  rect(500, 0, 100, 50);


  color cc = Colors.getComplimentary(a);
  fill(cc);
  rect(500, 55, 40, 40);

  color[] newColors = Colors.getSquare(a);
  float x = 500;
  float y = 100;
  float ht = 20;
  for (int i = 0; i < newColors.length; i++) {
    fill(newColors[i]);
    rect(x, y, 50, ht);
    y += ht;
  }


  newColors = Colors.getTetradic(a, 45);
  x = 550;
  y = 100;
  ht = 20;
  for (int i = 0; i < newColors.length; i++) {
    fill(newColors[i]);
    rect(x, y, 50, ht);
    y += ht;
  }

  newColors = Colors.getTriadic(a);
  x = 500;
  y = 185;
  ht = 20;
  for (int i = 0; i < newColors.length; i++) {
    fill(newColors[i]);
    rect(x, y, 50, ht);
    y += ht;
  }

  newColors = Colors.getSplitComplementary(a, 20);
  x = 550;
  y = 185;
  ht = 20;
  for (int i = 0; i < newColors.length; i++) {
    fill(newColors[i]);
    rect(x, y, 50, ht);
    y += ht;
  }

  newColors = Colors.getEvenDivisions(a, 10);
  x = 500;
  y = 250;
  ht = 20;
  for (int i = 0; i < newColors.length; i++) {
    fill(newColors[i]);
    rect(x, y, 50, ht);
    y += ht;
  }

  newColors = Colors.getEvenDivisions(a, 10, -80);
  x = 550;
  y = 250;
  ht = 20;
  for (int i = 0; i < newColors.length; i++) {
    fill(newColors[i]);
    rect(x, y, 50, ht);
    y += ht;
  }

  String[] values = Colors.getColorValue(a);
  textAlign(LEFT, TOP);
  fill(255);
  for (int i = 0; i < values.length; i++) {
    text(values[i], 20, 20 + i * 20);
  }


  x = 150;
  y = 100;
  for (int i = 0; i < myColors.size (); i++) {
    myColors.get(i).drawColors(x, y, color(0));
    y = 100;
    x += 100;
  }
} // end draw


//
void keyReleased() {
  if (key == 'd') {
    Colors.messagesOn = !Colors.messagesOn;
  }
  if (key == 't') {
    Colors.writeColorTemplate();
  }
  if (key == ' ') {
    loadColors();
  }
  if (key == 'a') {
    if (myColors.size() > 0) {
      myColors.get(0).addColor("ocean", color(0, 150, 255));
    }
  }
} // end keyReleased 

//
void loadColors() {
  myColors.clear();
  String[] rawFiles = OCRUtils.getFileNames(sketchPath("") + "sampleColors/", false);
  println("read in " + rawFiles.length);
  for (String s : rawFiles) {
    println("try for: " + s);
    Colors newColor = new Colors();
    newColor.readColors(s);
    if (newColor.valid) myColors.add(newColor);
  }
} // end loadColors
//
//
//
//
//
//
//

