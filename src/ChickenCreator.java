public class ChickenCreator
{
    public static void main(String[] args)
    {
        ChickenMaker maker = new ChickenMaker();

        maker.makeChicken(args[0], args[1]);
    }
}