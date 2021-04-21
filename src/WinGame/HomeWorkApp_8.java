//Дисциплина: Java.Уровень 1
//Домашнее задание №: 8 "Написание приложения с графическим интерфейсом"
//Студент: Алексей Пирогов
//Дата: 21.04.2021 15:00

package WinGame; // Код приложения оформлен в пакет WinGame

// Подключение пакетов:
import java.awt.event.ActionEvent;      // awt.event.ActionEvent для обработки событий
import java.awt.event.ActionListener;   // awt.event.ActionListener для обработки событий
import java.awt.event.MouseAdapter;     // awt.event.MouseAdapter для работы с мышкой
import java.awt.event.MouseEvent;       // awt.event.MouseEvent для генерации событий мышкой
import java.io.IOException;             // io.IOException для io
import javax.imageio.ImageIO;           // imageio.ImageIO для io графики
import javax.swing.*;                   // swing для работы с графикой
import java.awt.*;                      // awt (Abstract Window Toolkit)
                                        // AWT отвечает за работу с цветами, шрифтами, отрисовку графических примитивов, менеджеров компоновки

// Объявление класса HomeWorkApp_8, который является наследником класса JFrame,
// что позволяет использовать ввести новый тип данных HomeWorkApp_8 и работать с графикой
public class HomeWorkApp_8 extends JFrame { // класа

    // Объявление набора переменных для работы
    // с графической библиотекой:
    private static HomeWorkApp_8 game_window;   // переменная game_window типа HomeWorkApp_8 / JFrame, отвечающая за хранение окна (объекта окна)

    // Объявление набора переменных для
    // графических компонент приложения:
    private static Image background;            // переменная типа Image, которая отвечает за хранение фонового рисунка
    private static Image game_over;             // переменная типа Image, которая отвечает за хранение изображения о конце игры
    private static Image drop;                  // переменная типа Image, которая отвечает за хранение изображения капли

    // Объявление набора переменных для
    // хранения точек отображения капли:
    private static float drop_left = 200;       // кордината Х левого верхнего угла капли
    private static float drop_top = -200;       // кордината Y левого верхнего угла капли
    private static long last_frame_time;        // переменная last_frame_time, отвечающая за хранение
    private static float drop_speed = 100;      // переменная drop_speed, отвечающая за скорость перемещения капли
    private static int counter;                 // счётчик количества нажатий

    // В методе main генерируются исключения и происходит их обработка исключений,
    // поэтому требуется добавить оператор throws. Метод main не может сам обработать исключения
    public static void main(String[] args) throws IOException{

        // Cоздание экземпляра класса HomeWorkApp_8, наследника JFrame
        game_window = new HomeWorkApp_8();  // присвоене перменной game_windows ссылки на экземпляр класса HomeWorkApp_8

        // Предварительная настройка объекта окна, вызов ряда методов:
        game_window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);    // объявление константы для закрытия окна
        game_window.setLocation(200, 200);        // позиционирование окна относительно точки 0,0 - начала координат
        game_window.setSize(880, 470);      // установка размера окна в точках
        game_window.setResizable(false);                // запрет на изменение размера окна
        game_window.setTitle("Счёт: " + counter);       // объявление заголовка окна

        // Предустановка изображений для программы через вызов метода read() класса ImageIO:
        background = ImageIO.read(HomeWorkApp_8.class.getResourceAsStream("background.png"));
        game_over  = ImageIO.read(HomeWorkApp_8.class.getResourceAsStream("game_over.png"));
        drop  = ImageIO.read(HomeWorkApp_8.class.getResourceAsStream("drop.png"));

        // Инициализация переменной, необходимой для игрового цикла
        last_frame_time = System.nanoTime();     // получение системного времени в наносекундах

        // Cоздание объекта game_field - экземпляра класса PaintPanel, наследника JPanel
        PaintPanel game_field = new PaintPanel();

        // Добавление MouseListener к объекту game_field являющемуся наследником JPanel
        game_field.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                // super.mousePressed(e);
                // получение значений где произошел клик мышкой
                int xClick = e.getX();
                int yClick = e.getY();
                // определение правой и нижней границы капили
                float drop_right = drop_left + drop.getWidth(null);
                float drop_bottom = drop_top + drop.getHeight(null);
                // сопоставление координаты точки (XY) c точкой размещения капли
                boolean is_Drop = (xClick >= drop_left && xClick <= drop_right) && (yClick >= drop_top && yClick <= drop_bottom);
                // если выполняется условие, что клик был по точке где расположена капля, то
                if (is_Drop) {
                    counter++;                                  // икремент количества кликов
                    drop_top = -200;                            // смещение капли наверх, за экран
                    drop_left = (int)(Math.random() * (game_field.getWidth() - drop.getWidth(null)));   // сдвиг левее/правее по горизонту
                    drop_speed += 20;                           // приращение скорости
                    game_window.setTitle("Счёт: " + counter);   // обновление значение счётчика в заголовке экрана
                }
            }
        });

        // Создание экземпляра класса типа JButton
        JButton btn_left = new JButton("-");
        // Описание логики работы уменьшения скорости при нажатии на кнопку слева экрана
        btn_left.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                drop_speed -= 10;                                           // уменьшение скорости
                if (drop_speed < 0) drop_speed = Math.abs(drop_speed);      // если скорость отрицательная, то получение значения по модулю
                if (drop_speed == 0) drop_speed = 20;                       // если скорость равна 0, то увеличение скорости до 20
            }
        });

        // Создание экземпляра класса типа JButton
        JButton btn_right = new JButton("+");
        // Описание логики работы увеличения скорости при нажатии на кнопку справа экрана
        btn_right.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                drop_speed += 10;       // увеличение скорости на 10 единиц
            }
        });

        // Вывод элементов окна:
        game_window.add(btn_left, BorderLayout.WEST);   // вывод кнопок по увеличению и уменьшению скорости
        game_window.add(btn_right, BorderLayout.EAST);
        game_window.add(game_field);                    // вызов метода add() для добавления объекта типа JPanel к объекту HomeWorkApp_8
        game_window.setVisible(true);                   // вызов метода setVisible() объекта HomeWorkApp_8 для отрисовки окна
    }

    // Метод класса HomeWorkApp_8 для отображения графики в окне
    public static void paintOnPaintPanel(Graphics g) {
        g.drawImage(background, 0, 0, null);                       // отрисовка фонового изображения
        // Игровой цикл построеный на изменении времени и последующей отрисовки капли в новом месте

        long current_time = System.nanoTime();                                  // получение текущего системного времени
        float delta_time = (current_time - last_frame_time) * 0.000000001f;     // получение разницы между текущем временем и предыдущим значением, до создания объекта PaintPanel
        last_frame_time = current_time;                                         // изменение времени
        drop_top = drop_top + drop_speed * delta_time;                          // смещение капли по вертикали
        g.drawImage(drop, (int)drop_left, (int)drop_top, null);         // отрисовка капли с учётом обновлённых координат

        // Если верхняя точка расположения капли больше, чем верхняя граница, то вывод изображения, что настал конец игры
        if (drop_top > game_window.getHeight()) {
            g.drawImage(game_over, 240, 120, null);               // отрисовка изображения Game_over
        }
    }

    // Создание класса PaintPanel наследника JPanel для рисования внутри окна
    private static class PaintPanel extends JPanel {

        @Override   //переопределение метода paintComponent родительского класса JPanel
        protected void paintComponent(Graphics g){
            super.paintComponent(g);        // вызов родительского метода с объектом типа Graphics для отрисовки панели JPanel
            paintOnPaintPanel(g);           // вызов метода класса HomeWorkApp_8 наследника JFrame для отрисовки объекта Graphics
            repaint();                      // вызов родительского метода для отрисовки класса объекта класса JPanel
                                            // без вызова этого метода капля будет отрисовываться только при масштабировании окна,
                                            // а это запрещено исходя из условий задачи
        }
    }
}
