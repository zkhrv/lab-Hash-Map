public class TestHashMap
{
    public static void main(String[] args)
    {
        HashMap<Integer, String> test = new HashMap<>(5);
        test.AddElement(3, "Addel");
        test.AddElement(5, "Back");
        test.AddElement(1, "Lily");
        test.AddElement(9, "Janny");
        test.AddElement(8, "Sam");
        test.AddElement(2, "Madam");
        test.AddElement(6, "Jerry");
        test.AddElement(10, "John");
        test.AddElement(52, "Igor");
        test.AddElement(17, "Boby");
        test.AddElement(24, "Marta");

        test.print();
        test.SizeAndRatio();
        System.out.println("-----------------------------------");
        node testNode = test.GetPair(1);
        if (testNode.key != null)
            System.out.println("В хеш-таблице по ключу "+testNode.key+" лежат данные: "+testNode.data);
        System.out.println("-----------------------------------");
        test.DeleteElement(52);
        test.GetPair(52);
        System.out.println("-----------------------------------");
        test.ReHashing();
        test.print();
        test.SizeAndRatio();
        System.out.println("-----------------------------------");
        test.DeleteAll();
        test.SizeAndRatio();
    }
}