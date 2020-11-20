package springcore.annotations;

public class TestClass {

    @InjectRandomInt(max = 20)
    int randomField;
    int notRandomField = 15;
    String randomName;
    String randomSurname;
}