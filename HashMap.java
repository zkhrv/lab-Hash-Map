import java.util.ArrayList; // импорт класса
import java.util.LinkedList; // импорт класса
import java.util.stream.IntStream; // импорт класса

import static java.lang.Math.sqrt; // импорт метода sqrt

public class HashMap <T1 extends Comparable<T1>, T2> implements Comparable<T1> // объявление класса HashMap с двумя параметризированными типами
{
    private ArrayList<LinkedList<node<T1, T2>>> head = new ArrayList<>(); // создание поля head типа ArrayList, содержащего LinkedList элементов типа node<T1, T2>
    int m, allNumb=0; // объявление переменных m и allNumb типа int (число списков и пар соответственно)
    double a = 0.0; // объявление переменной a типа double, инициализированной нулевым значением (уровень загруженности)

    public HashMap(int m) // объявление конструктора с одним параметром
    {
        if (isPrime(m)) // проверка, является ли число m простым
        {
            this.m = m; // присвоение переменной m переданного значения
            for (int i=0; i<m; ++i) // инициализация каждого значения head значением null
                head.add(null);
        }
        else // если число m не простое
            System.out.println("Вы ввели недопустимый модуль!");
    }
    private boolean isPrime(int number) // объявление приватного метода с одним параметром типа int
    {
        return number > 1 // возвращает True, если переданное число больше 1
                && IntStream.rangeClosed(2, (int) sqrt(number)) // создание стрима int от 2 до квадратного корня из number включительно
                .noneMatch(n -> (number % n == 0)); // проверка, что ни одно число в созданном стриме не является делителем number
    }

    public void AddElement(Object key, T2 data) // объявление метода AddElement
    {
        int hashkey = CountHashKey(key, this.m); // создание переменной hashkey и присвоение ей значения, полученного при вызове метода CountHashKey с переданными параметрами

        if (hashkey > -1 && hashkey < m) // проверка, что hashkey находится в допустимых границах
        {
            ++allNumb; // инкрементирование значения переменной allNumb
            a = (double) allNumb / m; // пересчет загруженности а
            if (a <= 2) // если уровень загруженности не превышает 2, то добавляем элемент в соответствующий список
                head.set(hashkey, AddDelStr(head.get(hashkey),(T1) key, data, true));
            else // иначе перехешируем таблицу и добавим элемент
            {
                Rehashing(); // вызов метода Rehashing
                a = (double) allNumb / m; // пересчет уровня загруженности a после перехеширования
                hashkey = CountHashKey(key, this.m); // пересчет значения hashkey после перехеширования
                head.set(hashkey, AddDelStr(head.get(hashkey),(T1) key, data, true)); // добавление элемента в соответствующий список после перехеширования
            }
        }
        else // если hashkey находится в недопустимых границах, то
            System.out.println("Выявлена проблема с ключом " + key + "! Добавление элемента не произошло.");
    }
    private int CountHashKey(Object key, int mod) // Метод, который вычисляет хэш-ключ для заданного объекта и заданного модуля
    {
        int hashKey = -5; // значение хеш-ключа по умолчанию
        if (key instanceof String) // если объект является строкой, то вычисляем хеш для нее
        {
            String[] tmpMas = ((String) key).split(""); // разбиваем строку на массив символов
            long tmpkey = 0; // временная переменная для хранения хеш-ключа
            for (int i = 0; i < tmpMas.length; ++i) // проходим по всем символам
                tmpkey = tmpkey * 10 + TranslateToInt(tmpMas[i]); // вычисляем хеш-ключ
            hashKey = (int) (tmpkey % mod); // вычисляем итоговый хеш
        }
        else // если объект не является строкой, то вычисляем хеш-ключ по умолчанию
            hashKey = (int) key % mod; // вычисляем хеш-ключ
        return hashKey; // возвращаем хеш-ключ
    }
    private LinkedList<node<T1, T2>> AddDelStr(LinkedList<node<T1, T2>> orig, T1 key, T2 data, boolean marker) // Метод добавления и удаления элементов из связанного списка
    {
        if (marker) //  Если маркер равен true, то добавляем элемент в связанный список
        {
            if (orig == null) // Если связанный список пуст, создаем новый и добавляем в него элемент
            {
                LinkedList<node<T1, T2>> adding = new LinkedList<>(); // создаем новый связный список
                adding.add(new node(key, data)); // добавялем в него новый узел с новыми данными
                return adding; // возращаем новый связный список
            }
            else // Если связанный список не пуст, то добавляем в него новый узел с ключом и данными
            {
                orig.add(new node(key, data)); // добавляем в связный список новый узел с ключом и данными
                return orig; // возвращаем измененный связный список
            }
        }
        else // Если маркер равен false, то удаляем элемент из связанного списка по ключу
        {
            if (orig != null) // Если связанный список не пуст, то ищем узел с заданным ключом и удаляем его
            {
                for (int i=0; i<orig.size(); ++i) // Перебираем все узлы связанного списка
                    if (key.compareTo(orig.get(i).key) == 0) // Если находим узел с заданным ключом
                    {
                        orig.remove(i); // Удаляем его из связанного списка
                        break; // прерываем цикл
                    }
                return orig; // возвращаем изменный список
            }
            else // Если связанный список пуст, то выводим сообщение об ошибке
                System.out.println("В хеш-дереве нет значения по такому ключу.");
            return orig; // возвращаем неизмененный связный список
        }
    }
    private void Rehashing() // Метод для перехеширования при достижении порогового значения заполненности хеш-таблицы
    {
        int newM = this.m; // Новый размер хеш-таблицы
        boolean marker = true; // Маркер для проверки, является ли новый размер простым числом
        do // пока не найдено простое число
        {
            int tmp = newM*2+1; // увеличиваем размер вдвое и прибавляем единицу
            if (isPrime(tmp)) // если новый размер является простым числом
            {
                marker = false; // Устанавливаем маркер в false, чтобы прекратить поиск
                newM = tmp; // устанавливаем новый размер
            }
            else // иначе
                ++newM; // Увеличиваем размер на единицу и продолжаем поиск
        }
        while (marker); // Повторяем, пока не найдено простое число

        ArrayList<LinkedList<node<T1, T2>>> tmpHead = new ArrayList<>(); // Создаем временный список для новых элементов
        for (int i=0; i<newM; ++i) // заполняем список null-ами
            tmpHead.add(null);

        for (int i=0; i<m; ++i) // проходим по всем элементам старой таблицы
        {
            int size = 0; // размер списка на данной позиции
            if (head.get(i) != null) // если список не пуст
                size = head.get(i).size(); // устанавливаем размер списка
            for (int j=0; j<size; ++j) // проходим по всем элементам списка
            {
                node tmpNode = head.get(i).get(j); // получаем текущий элемент списка
                int NewHashKey = CountHashKey(tmpNode.key, newM); // вычисляем новый хеш-ключ для элемента
                tmpHead.set(NewHashKey, AddDelStr(tmpHead.get(NewHashKey), (T1) tmpNode.key, (T2) tmpNode.data, true)); // добавляем элемент в новую хеш-таблицу
            }
        }

        this.head = tmpHead; // заменяем старый список на новый
        this.m = newM; // обновляем размер таблицы
    }

    public void DeleteAll() // метод удаления всех элементов из хеш-таблицы
    {
        if (head == null || allNumb == 0) // Если хеш-таблица пуста или количество элементов в ней равно 0
            System.out.println("Таблица и так пуста. Остановись!");
        for (int i=0; i<head.size(); ++i) // проходим по всем элементам хеш-таблицы
            head.set(i, null); // удаляем каждый список с элементами
        System.out.println("Хеш-таблица очищена!");
        a = 0.0; //  Обнуляем среднюю заполненность таблицы
        allNumb = 0; // Обнуляем количество элементов в таблице
    }
    public node GetPair(T1 key) // получение пары по ключу
    {
        node answerNode = new node(null, null); // создание новой пары ключ-значения
        int hashKey = CountHashKey(key, this.m); // вычисление хеш-ключа
        int marker = 0, size = 0; // установка маркера и размера 0
        if (head.get(hashKey) != null) // если существует список по данному хеш-ключу
            size = head.get(hashKey).size(); // размер устанавливается равным размеру головного элемента
        for (int j=0; j<size; ++j) // для всех элементов данного списка
            if (key.compareTo(head.get(hashKey).get(j).key) == 0) // если ключ соответствует данному ключу
            {
                marker = 1; // устанавливается маркер в 1
                answerNode = head.get(hashKey).get(j); //  текущая пара записывается в ответ
                break; // выход из цикла
            }

        if (marker == 0) // если маркер остался равным 0
            System.out.println("В хеш-дереве нет значения по такому ключу");
        return answerNode; // возвращаем ответ
    }

    private int TranslateToInt(String orig) // перевод первого символа строки в целочисленный тип
    {
        int answer = -5; // объявление переменной answer и присваивание ей значения -5
        char c = orig.charAt(0); // получение первого символа строки orig и запись его в переменную c типа char
        answer = (int) c; // приведение символа к целочисленному значению и запись его в переменную answer
        return answer; //  возврат значения переменной answer
    }
    public void DeleteElement(T1 key) // метод для удаления по ключу из хеш-таблицы
    {
        if (head == null || allNumb == 0) // Если таблица пуста, выводим сообщение и выходим из метода
            System.out.println("Таблица и так пуста");
        else // иначе
        {
            int hashKey = CountHashKey(key, this.m); // вычисляем хеш-ключ
            int size = 0; // Инициализируем переменную для хранения размера списка по текущему хеш-коду
            boolean marker = true; // Инициализируем переменную-маркер, которая поможет определить, был ли найден элемент по ключу
            if (head.get(hashKey) != null) // Если в таблице есть список элементов по текущему хеш-коду, записываем его размер в переменную size
                size = head.get(hashKey).size();
            for (int j=0; j<size; ++j) // Проходим по всем элементам списка по текущему хеш-коду
                if (key.compareTo(head.get(hashKey).get(j).key) == 0) // Если найден элемент с ключом, равным искомому, то удаляем его из списка
                {
                    marker = false; // Устанавливаем маркеру false
                    head.set(hashKey, AddDelStr(head.get(hashKey), key, null, false)); // Удаляем элемент из списка по ключу
                    System.out.println("Значение по ключу "+key+" удалено из таблицы.");
                    -- allNumb; // Уменьшаем счетчик числа элементов в таблиц
                    a = (double) allNumb/m; // Пересчитываем коэффициент загрузки таблицы
                    break; // Останавливаем цикл
                }

            if (marker) // Если элемент не найден, выводим сообщение об ошибке
                System.out.println("В хеш-дереве нет значения по такому ключу");
        }
    }
    public void ReHashing() // метод для перехеширования по желанию пользователя
    {
        Rehashing();
    }
    public void print() // вывод хеш-таблицы
    {
        System.out.println("/------------------------------|");
        for (int i=0; i<m; ++i) // проход по всем хешам
        {
            System.out.print("По хешу "+i+" хранятся данные: "); // вывод информации о текущем хеше
            int size = 0; // размер списка, хранящегося по текущему хешу
            if (head.get(i) != null) // если список не пустой
                size = head.get(i).size(); // определить его размер
            for (int j = 0; j < size; ++j) // проход по элементам списка
            {
                System.out.print(head.get(i).get(j).key+" - "+head.get(i).get(j).data+"; "); // вывод ключа и значения элемента
            }
            System.out.println("");
        }
        System.out.println("|------------------------------\\");
    }
    public void SizeAndRatio() // вывод основных параметров
    {
        System.out.println("Пар в хеш-таблице: "+allNumb);
        System.out.println("Число списков: "+m);
        System.out.println("Вещественный коэффициент: "+a);
    }

    @Override // Аннотация, указывающая на переопределение метода интерфейса или класса-родителя
    public int compareTo(T1 o) // Переопределение метода сравнения для типа T1
    {
        return 0; // возвращаем 0 => объекты равны
    }
}
