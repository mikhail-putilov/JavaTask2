import java.util.*;

/**
 * <p>
 * Придумал наиболее наглядное представление алгоритма:
 * Описание:
 * <dl>
 *     <dt>5 6 7 8 0 1</dt>
 *     <dd>Номера элементов в порядке их следования</dd>
 *     <dt>free</dt>
 *     <dd>Свободный номер. Номер, который не принадлежит в данный момент никакому элементу</dd>
 *     <dt>Освобождать какой-то номер n</dt>
 *     <dd>Присвоить номер free элементу c номером n (тем самым освободить номер n).</dd>
 * </dl>
 * </p>
 * Шаги:
 * ↓
 * 5 6 7 8 0 1 mem: 5 6 7 8 0 1
 * free = 2
 * хотим поставить 1, освобождаем занятую единцу
 * ----------
 * ↓
 * 5 6 7 8 0 2 mem 5 6 7 8 0 1
 * free = 2
 * ставим 1 и обновляем free и переходим к след.
 * ----------
 *   ↓
 * 1 6 7 8 0 2 mem 5 6 7 8 0 1
 * free = 5
 *
 * improvement:
 * поставив единицу, обновляем free, и не переходим к след. а проверяем, свободен ли
 * нужный номер для элемента, чей номер мы освободили
 * тогда:
 * ↓
 * 5 6 7 8 0 1
 * free = 2
 * хотим поставить 1, освобождаем занятую единцу
 * ----------
 * ↓         f
 * 5 6 7 8 0 2
 * free = 2
 * ставим 1 и обновляем free
 * ----------
 * ↓         f
 * 1 6 7 8 0 2
 * free = 5
 * Есть возможность сразу поставить верный номер у элемента с маркером f.
 * Если такая возможность есть, то ставим. иначе переходим к след. элементу.
 * ----------
 * ↓
 * 1 6 7 8 0 2
 * free = 5
 * Хотим поставить 2, особождаем 2
 * ----------
 * ↓       f
 * 1 6 7 8 0 5
 * free = 5
 * Ставим 2 и обновляем free
 * ----------
 * ↓       f
 * 1 2 7 8 0 2
 * free = 6
 * Есть возможность поставить нужный номер у элемента с маркером f? Есть. Ставим.
 * ----------
 * ↓       f
 * 1 2 7 8 0 6
 * free = 5
 * Передвигаем стрелку.
 * ----------
 * ↓
 * 1 2 7 8 0 6
 * free = 5
 * Хотим поставить 3 …
 */

public class Task2Impl implements IElementNumberAssigner {

    // ваша реализация должна работать, как singleton. даже при использовании из нескольких потоков.
    public static final IElementNumberAssigner INSTANCE = new Task2Impl();
    // memory holds key-value pairs (<element.getNumber(),element> pairs) for fast searching
    // element by its number
    private final NavigableMap<Integer, IElement> memory = new TreeMap<>();
    private int freeNumber = 0;

    @Override
    public void assignNumbers(final List<IElement> elements) {
        //preprocess:
        for (IElement e : elements)
            memory.put(e.getNumber(), e);
        setFreeNumber();

        assignNumbersHelper(elements);
    }


    private void assignNumbersHelper(final List<IElement> elements) {
        int counter = 1;

        for (IElement current : elements) {
            int currentNumber = current.getNumber();
            //current number must be equals mustBeNumber
            if (currentNumber != counter) {
                // try find element with number <<mustBeNumber>>
                IElement elementHoldsCounter = findElementByNumber(counter);
                if (elementHoldsCounter != null)
                    rearrangeNumbers(current, elementHoldsCounter);
                else // counter is a free number, so just set it up
                    setupNumberAndUpdateMemory(current, counter);
            }
            counter++;
        }
    }

    /**
     * Free element. Memory and freeNumber are also updated properly
     * @param element element which number will be set to freeNumber
     * @return number which was holden by given element (typically that number is mustBeNumber)
     */
    private int freeElement(final IElement element) {
        int numberSave = element.getNumber();
        //remove modified element
        memory.remove(numberSave);
        
        element.setupNumber(freeNumber);
        memory.put(element.getNumber(), element);
        //update free
        freeNumber = numberSave;
        return numberSave;
    }
    
    /**
     * Find element in sorted memory
     * @param number search key
     * @return element which is paired with given number or null if element is not found
     */
    private IElement findElementByNumber(final int number) {
        return memory.get(number);
    }

    /**
     *
     * @param tobeSetup element which takes number of the second argument
     * @param tobeFree element which number will be set to free number
     */
    private void rearrangeNumbers(final IElement tobeSetup, final IElement tobeFree) {
        //free element and get his number
        int tobeSetupNumber = freeElement(tobeFree);

        //will be erased so tobeSetup.getNumber() will be freeNumber
        freeNumber = tobeSetup.getNumber();
        setupNumberAndUpdateMemory(tobeSetup, tobeSetupNumber);
    }

    private void setupNumberAndUpdateMemory(final IElement element, int newNumber) {
        memory.remove(element.getNumber());
        element.setupNumber(newNumber);
        memory.put(element.getNumber(), element);
    }

    /**
     * Finds free number in memory.
     * Memory must be correct: for all element in memory : element.getNumber() == key
     */
    private void setFreeNumber() {
        int free = Integer.MIN_VALUE;
        Set<Integer> numbers = memory.keySet();
        while (numbers.contains(free)) {
            free++;
        }
        freeNumber = free;
    }
}
