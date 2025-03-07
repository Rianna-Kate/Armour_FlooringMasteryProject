package com.sg.flooring;

import com.sg.flooring.controller.FlooringController;
import com.sg.flooring.dao.*;
import com.sg.flooring.service.FlooringServiceLayer;
import com.sg.flooring.service.FlooringServiceLayerImpl;
import com.sg.flooring.ui.FlooringView;
import com.sg.flooring.ui.UserIO;
import com.sg.flooring.ui.UserIOConsoleImpl;

public class App {
    public static void main(String[] args) {
        UserIO myIo = new UserIOConsoleImpl();
        FlooringView view = new FlooringView(myIo);
        OrderDao orderDao = new OrderDaoFileImpl();
        ProductDao productDao = new ProductDaoFileImpl();
        StateTaxDao stateTaxDao = new StateTaxDaoFileImpl();
        FlooringServiceLayer service = new FlooringServiceLayerImpl(orderDao, productDao, stateTaxDao);

        FlooringController controller =
                new FlooringController(service, view);

        controller.run();
    }
}
