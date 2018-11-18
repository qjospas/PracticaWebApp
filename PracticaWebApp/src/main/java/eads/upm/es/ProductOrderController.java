package eads.upm.es;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ProductOrderController {

	@Autowired
	OrderRepository orders;
	@Autowired
	ProductRepository productsRepo;
	static final Random random= new Random();
	
	@GetMapping("/")
	public String getOrders(Model model)
	{
		model.addAttribute(orders.findAll());
		return "orders";
	}
	
	@PostMapping("/newOrder")
	public String getOrders(Model model, @RequestParam String name , @RequestParam String[] products )
	{
		ProductOrder order= new ProductOrder();
		order.setName(name);
		for ( String product : products)
		{
			Product prod= new Product();
			prod.setName(product);
			prod.setDescription("Descripción " + product);
			prod.setDispatched(false);	
			double price= BigDecimal.valueOf(random.nextDouble()*10)
		    .setScale(2, RoundingMode.HALF_UP).doubleValue();
			prod.setPrice(price);
			order.getProducts().add(prod);
		}
		orders.save(order);
		model.addAttribute(orders.findAll());
		return "orders";
	}
	
	@GetMapping("/order/{id}")
	public String getOrder(Model model, @PathVariable Long id)
	{
		ProductOrder order= orders.findById(id).get();
		model.addAttribute( order);
		return "order";
	}
	
	@GetMapping("/modify/{id}")
	public String getModifyOrder(Model model, @PathVariable Long id)
	{
		ProductOrder order= orders.findById(id).get();
		model.addAttribute( order);
		return "modify";
	}
	
	@PostMapping("/saveOrder/{id}")
	public String saveOrder(Model model, @PathVariable Long id,  @RequestParam String name , @RequestParam String[] products ,  @RequestParam long[] ids   )
	{
		ProductOrder order= orders.findById(id).get();
		order.setName(name);
		order.setProducts(new ArrayList<Product>());
		int idx=0;
		for ( long idI : ids)
		{
			Optional<Product> productOpt = productsRepo.findById(idI);
			if ( productOpt.isPresent() )
			{
				Product prod= productOpt.get();
				prod.setName(products[idx]);
				order.getProducts().add(prod);
			}else
			{
				Product prod= new Product();
				prod.setName(products[idx]);
				prod.setDescription("Descripción " + products[idx]);
				prod.setDispatched(false);	
				double price= BigDecimal.valueOf(random.nextDouble()*10)
			    .setScale(2, RoundingMode.HALF_UP).doubleValue();
				prod.setPrice(price);
				productsRepo.save(prod);
				order.getProducts().add(prod);
				
			}
			idx++;
		}
		System.out.println(order.toString());
		orders.saveAndFlush(order);
		model.addAttribute( order);
		return "order";
	}
	
	@PostMapping("/dispatchOrder/{id}")
	public String dispatch(Model model, @PathVariable Long id,  @RequestParam String name , @RequestParam long[] ids , @RequestParam Boolean[] dispatched )
	{
		ProductOrder order= orders.findById(id).get();
		order.setName(name);
		order.setProducts(new ArrayList<Product>());
		int idx=0;
		for ( long idI : ids)
		{
			Product prod= productsRepo.findById(idI).get();
			prod.setDispatched(dispatched[idx]);	
			order.getProducts().add(prod);
			idx++;
		}
		orders.saveAndFlush(order);
		model.addAttribute( order);
		return "order";
	}
	
	@GetMapping("/delete/{id}")
	public ModelAndView deleteOrder(ModelMap model, @PathVariable Long id)
	{
		orders.deleteById(id);
		model.addAttribute(orders.findAll());
		return new ModelAndView("redirect:/", model);
	}
	

}
