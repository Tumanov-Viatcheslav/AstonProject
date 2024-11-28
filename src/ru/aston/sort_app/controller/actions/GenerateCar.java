package ru.aston.sort_app.controller.actions;

import ru.aston.sort_app.controller.MenuAction;
import ru.aston.sort_app.core.Car;
import ru.aston.sort_app.core.UserInputChoice;
import ru.aston.sort_app.services.Service;
import ru.aston.sort_app.view.InputValidator;
import ru.aston.sort_app.view.MessagePrinter;
import ru.aston.sort_app.view.Reader;

import java.util.List;

public class GenerateCar implements MenuAction {
    private final MessagePrinter messagePrinter;
    private final Reader reader;

    private final Service<Car> carService;
    private final List<Car> cars;
    private int count;

    public GenerateCar(MessagePrinter messagePrinter, Reader reader, Service<Car> carService, List<Car> cars) {
        this.messagePrinter = messagePrinter;
        this.reader = reader;
        this.carService = carService;
        this.cars = cars;
        count = 0;
    }

    @Override
    public void execute(UserInputChoice choice) {
        if (choice == UserInputChoice.ACTION_CAR_MANUAL_GENERATED) {
            messagePrinter.printMessage("Введите данные для автомобиля. Для завершения ввода введите 'exit' в поле модели.");
            while (true) {
                messagePrinter.printMessage("Введите модель:");
                String model = reader.getStringInput();
                if ("exit".equalsIgnoreCase(model)) {
                    break;
                }
                messagePrinter.printMessage("Введите мощност:");
                int power = Integer.parseInt(reader.getStringInput());

                messagePrinter.printMessage("Введите год:");
                int year = Integer.parseInt(reader.getStringInput());

                Car car = new Car.Builder()
                        .setModel(model)
                        .setPower(power)
                        .setYear(year)
                        .build();
                carService.add(car);
                count++;
            }
            cars.clear();
            cars.addAll(carService.generate(choice, count));
            messagePrinter.printMessage(cars.toString());
        } else {
            messagePrinter.printMessage("Введите количество элементов (не более 30):");
            int size = InputValidator.getValidatedInput(30);
            cars.addAll(carService.generate(choice, size));
            messagePrinter.printMessage(cars.toString());
        }
    }
}
