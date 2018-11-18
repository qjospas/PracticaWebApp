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
		order.setName("Pedido diario");
		Product leche= new Product();
		leche.setName("Leche");
		leche.setDescription("Leche fresca");
		leche.setDispatched(false);
		order.getProducts().add(buildProduct("Leche Pascual" , "Leche Entera Pascual"));
		order.getProducts().add(buildProduct("Huevos Camperos" , "Docena de huevos XL"));
		order.getProducts().add(buildProduct("Harina" , "Harina de repostería"));
		order.getProducts().add(buildProduct("Pan de molde" , "Bimbo Familiar"));
		orders.save(order);
	}
	
	public static Product buildProduct(String name, String description)
	{
		Product product= new Product();
		product.setName(name);
		product.setDescription(description);
		product.setDispatched(false);	
		double price= BigDecimal.valueOf(random.nextDouble()*10)
			    .setScale(2, RoundingMode.HALF_UP).doubleValue();
		product.setPrice(price);
		return product;
	}

}
