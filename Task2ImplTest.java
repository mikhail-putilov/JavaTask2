import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Task2ImplTest {
    private final ElementExampleImpl.Context context = new ElementExampleImpl.Context();

    @org.junit.Test
    public void testAssignNumbers() throws Exception {
        List<IElement> elems = new ArrayList<>();
        Collections.addAll(elems, new ElementExampleImpl(context, 1),
                new ElementExampleImpl(context, -1),new ElementExampleImpl(context, 3),new ElementExampleImpl(context, 0));
        Task2Impl.INSTANCE.assignNumbers(elems);
        System.out.println(elems);
    }
}
