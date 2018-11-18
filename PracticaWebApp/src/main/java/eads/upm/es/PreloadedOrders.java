package eads.upm.es;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Controller;

@Controller
public class PreloadedOrders implements CommandLineRunner {

	@Autowired
	private OrderRepository orders;
	static final Random random= new Random();
	
	@Override
	public void run(String... args) throws Exception {
		ProductOrder order= new ProductOrder();
		order.setName("Pedido de productos frescos");

		order.getProducts().add(buildProduct("Leche Pascual" , "Leche Entera Pascual 1L" , 0.89));
		order.getProducts().add(buildProduct("Huevos de Corral" , "Coren. 6 huevos frescos de gallinas camperas" , 1.59));
		order.getProducts().add(buildProduct("Cogollo Bio" , "Cogollo Carrefour Bio 2 piezas", 1.89));
		order.getProducts().add(buildProduct("Pollo de Corral" , "Pollo de Corral Coren 1,8 Kg" , 8.28));
		order.getProducts().add(buildProduct("Jamón Iberico de Bellota" , "Jamón Joselito 9kg" , 700.0 ));
		orders.save(order);
		
		order= new ProductOrder();
		order.setName("Pedido de Baño e Higiene personal");
		order.getProducts().add(buildProduct("Gel de ducha" , "Gel Moussant, Moussel", 4.67));
		order.getProducts().add(buildProduct("Champú fortificante Pure Fresh " , "Garnier-Fructis" , 3.85));
		order.getProducts().add(buildProduct("Dentífrico Pro-Expert Multi-Protección Menta Fresca" , "Oral-B" , 4.95));
		order.getProducts().add(buildProduct("Enjuague bucal triple acción" , "Carrefour", 1.25));
		orders.save(order);
	}
	
	public static Product buildProduct(String name, String description)
	{
		double price= BigDecimal.valueOf(random.nextDouble()*10)
			    .setScale(2, RoundingMode.HALF_UP).doubleValue();
		return buildProduct( name,  description , price );
	}
	
	
	public static Product buildProduct(String name, String description , double price)
	{
		Product product= new Product();
		product.setName(name);
		product.setDescription(description);
		product.setDispatched(false);	
		product.setPrice(price);
		return product;
	}

}
