package com.example.session15.controller;

import com.example.session15.model.*;
import com.example.session15.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

@Controller
public class ProductReviewController {

    private static final Logger LOGGER = Logger.getLogger(ProductReviewController.class.getName());
    private final ProductRepository productRepository;
    private final ReviewRepository reviewRepository;
    private final CartRepository cartRepository;
    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;

    @Autowired
    public ProductReviewController(ProductRepository productRepository,
                                   ReviewRepository reviewRepository,
                                   CartRepository cartRepository,
                                   OrderRepository orderRepository,
                                   OrderDetailRepository orderDetailRepository) {
        this.productRepository = productRepository;
        this.reviewRepository = reviewRepository;
        this.cartRepository = cartRepository;
        this.orderRepository = orderRepository;
        this.orderDetailRepository = orderDetailRepository;
    }

    @GetMapping("/products")
    public String listProducts(Model model) {
        model.addAttribute("products", productRepository.findAll());
        return "listProduct";
    }

    @GetMapping("/product/{id}")
    public String productDetail(@PathVariable("id") String id, Model model) {
        Product product = productRepository.findById(id);
        if (product == null) {
            throw new IllegalArgumentException("Sản phẩm không tồn tại");
        }
        model.addAttribute("product", product);
        model.addAttribute("reviews", reviewRepository.findByIdProduct(id));
        model.addAttribute("review", new Review());
        model.addAttribute("cart", new Cart());
        return "productDetail";
    }

    @PostMapping("/product/{id}/review")
    public String addReview(@PathVariable("id") String id,
                            @ModelAttribute("review") Review review,
                            RedirectAttributes redirectAttributes) {

        review.setIdProduct(id);
        if (review.getIdUser() == null || review.getIdUser().trim().isEmpty()) {
            review.setIdUser("anonymous");
        }
        if (review.getRating() <= 0) {
            review.setRating(1);
        }

        try {
            if (reviewRepository.save(review)) {
                redirectAttributes.addFlashAttribute("message", "đánh giá sản phẩm thành công!");
            } else {
                redirectAttributes.addFlashAttribute("error", "không thể đánh giá sản phẩm, vui lòng thử lại.");
            }
        } catch (Exception e) {
            LOGGER.severe("Error saving cart: " + e.getMessage());
            redirectAttributes.addFlashAttribute("error", "lỗi" + e.getMessage());
        }

        return "redirect:/product/" + id + "/reviews";
    }

    @GetMapping("/product/{id}/reviews")
    public String viewReviews(@PathVariable("id") String id, Model model) {
        Product product = productRepository.findById(id);
        if (product == null) {
            throw new IllegalArgumentException("Sản phẩm không tồn tại");
        }
        model.addAttribute("product", product);
        model.addAttribute("reviews", reviewRepository.findByIdProduct(id));
        return "reviews";
    }

    @PostMapping("/product/{id}/add-to-cart")
    public String addToCart(@PathVariable("id") String id,
                            @ModelAttribute("cart") Cart cart,
                            RedirectAttributes redirectAttributes) {
        Product product = productRepository.findById(id);
        if (product == null) {
            throw new IllegalArgumentException("Sản phẩm không tồn tại");
        }

        cart.setIdProduct(id);
        if (cart.getIdUser() == null || cart.getIdUser().trim().isEmpty()) {
            cart.setIdUser("anonymous");
        }
        if (cart.getQuantity() <= 0) {
            cart.setQuantity(1);
        }

        try {
            if (cartRepository.save(cart)) {
                redirectAttributes.addFlashAttribute("message", "Sản phẩm đã được thêm vào giỏ hàng!");
            } else {
                redirectAttributes.addFlashAttribute("error", "Không thể thêm vào giỏ hàng, vui lòng thử lại.");
            }
        } catch (Exception e) {
            LOGGER.severe("Error saving cart: " + e.getMessage());
            redirectAttributes.addFlashAttribute("error", "Lỗi hệ thống khi lưu giỏ hàng: " + e.getMessage());
        }
        return "redirect:/cart/" + cart.getIdUser();
    }

    @GetMapping("/cart/{idUser}")
    public String viewCart(@PathVariable("idUser") String idUser, Model model) {
        List<Cart> carts = cartRepository.findByIdUser(idUser);
        List<CartItem> cartItems = new ArrayList<>();
        double totalPrice = 0.0;

        for (Cart cart : carts) {
            Product product = productRepository.findById(cart.getIdProduct());
            if (product != null) {
                cartItems.add(new CartItem(cart, product));
                totalPrice += product.getPrice() * cart.getQuantity();
            }
        }

        model.addAttribute("cartItems", cartItems);
        model.addAttribute("totalPrice", totalPrice);
        model.addAttribute("idUser", idUser);
        return "cart";
    }

    @GetMapping("/checkout/{idUser}")
    public String viewCheckout(@PathVariable("idUser") String idUser, Model model) {
        List<Cart> carts = cartRepository.findByIdUser(idUser);
        List<CartItem> cartItems = new ArrayList<>();
        double totalPrice = 0.0;

        for (Cart cart : carts) {
            Product product = productRepository.findById(cart.getIdProduct());
            if (product != null) {
                cartItems.add(new CartItem(cart, product));
                totalPrice += product.getPrice() * cart.getQuantity();
            }
        }

        model.addAttribute("cartItems", cartItems);
        model.addAttribute("totalPrice", totalPrice);
        model.addAttribute("idUser", idUser);
        model.addAttribute("order", new Order());
        return "checkout";
    }

    @PostMapping("/checkout/{idUser}/place-order")
    public String placeOrder(@PathVariable("idUser") String idUser,
                             @ModelAttribute("order") Order order,
                             RedirectAttributes redirectAttributes) {
        LOGGER.info("Placing order for user: " + idUser);
        LOGGER.info("Order: " + order);

        // Generate unique orderId
        String orderId = UUID.randomUUID().toString();
        order.setOrderId(orderId);
        order.setIdUser(idUser);

        try {
            // Save order
            orderRepository.save(order);

            // Save order details from cart
            List<Cart> carts = cartRepository.findByIdUser(idUser);
            for (Cart cart : carts) {
                Product product = productRepository.findById(cart.getIdProduct());
                if (product != null) {
                    OrderDetail orderDetail = new OrderDetail();
                    orderDetail.setOrderId(orderId);
                    orderDetail.setProductId(cart.getIdProduct());
                    orderDetail.setQuantity(cart.getQuantity());
                    orderDetail.setCurrentPrice(product.getPrice());
                    orderDetailRepository.save(orderDetail);
                }
            }

            // Clear cart after order placement
            for (Cart cart : carts) {
                cartRepository.delete(cart);
            }

            redirectAttributes.addFlashAttribute("message", "Đặt hàng thành công!");
            return "redirect:/products";
        } catch (Exception e) {
            LOGGER.severe("Error placing order: " + e.getMessage());
            redirectAttributes.addFlashAttribute("error", "Lỗi hệ thống khi đặt hàng: " + e.getMessage());
            return "redirect:/checkout/" + idUser;
        }
    }

    @GetMapping("/orders")
    public String listOrders(Model model) {
        List<Order> orders = orderRepository.findAll();
        model.addAttribute("orders", orders);
        return "orders";
    }

    @GetMapping("/order/{orderId}")
    public String viewOrderDetail(@PathVariable("orderId") String orderId, Model model) {
        Order order = orderRepository.findById(orderId);
        if (order == null) {
            model.addAttribute("error", "Đơn hàng không tồn tại");
            return "orders";
        }

        List<OrderDetail> orderDetails = orderDetailRepository.findByOrderId(orderId);
        List<OrderItem> orderItems = new ArrayList<>();
        double totalPrice = 0.0;

        for (OrderDetail orderDetail : orderDetails) {
            Product product = productRepository.findById(orderDetail.getProductId());
            if (product != null) {
                orderItems.add(new OrderItem(orderDetail, product));
                totalPrice += orderDetail.getQuantity() * orderDetail.getCurrentPrice();
            }
        }

        model.addAttribute("order", order);
        model.addAttribute("orderItems", orderItems);
        model.addAttribute("totalPrice", totalPrice);
        return "orderDetail";
    }
}