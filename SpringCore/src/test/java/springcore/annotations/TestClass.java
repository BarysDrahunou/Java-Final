package springcore.annotations;

public class TestClass {

    @InjectRandomInt(max = 20)
    int randomField;
    int notRandomField = 15;
    @RandomName
    String randomName;
    @RandomSurname
    String randomSurname;
}