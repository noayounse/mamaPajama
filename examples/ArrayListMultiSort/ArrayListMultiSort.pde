import ocrUtils.maths.*;
import ocrUtils.*;
import ocrUtils.ocr3D.*;

class Thing {
  float num = random(100);
} // end class THing

class Other {
  String letter = "";
  Other(String s) {
    letter = s;
  }
} // end class THing


ArrayList<Thing> things = new ArrayList<Thing>();
ArrayList<Other> others = new ArrayList<Other>();
ArrayList<Other> moreOthers = new ArrayList<Other>();

void setup() {

  OCRUtils.begin(this);

  for (int i = 0; i < 10; i++) {
    things.add(new Thing());
    String newLetter = Character.toString((char)(i + 65));
    others.add(new Other(newLetter));
    newLetter = Character.toString((char)(89 - i));

    moreOthers.add(new Other(newLetter));

    print(things.get(i).num + " -- ");
    print(others.get(i).letter + " -- ");
    println(moreOthers.get(i).letter);
  }

  println("__- sort - __");
  
  //OCRUtils.sortObjectArrayListsSimple(things, "num", others, moreOthers);
  OCRUtils.sortObjectArrayListsSimple(moreOthers, "letter", others, things);

  for (int i = 0; i < things.size(); i++) {
    print(things.get(i).num + " -- ");
    print(others.get(i).letter + " -- ");
    println(moreOthers.get(i).letter);
  }
  exit();
} // end setup

