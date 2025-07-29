package com.wildan.e_commerce.services;

import com.wildan.e_commerce.exceptions.APIException;
import com.wildan.e_commerce.exceptions.ResourceNotFoundException;
import com.wildan.e_commerce.model.Category;
import com.wildan.e_commerce.model.Product;
import com.wildan.e_commerce.model.User;
import com.wildan.e_commerce.payload.ProductDTO;
import com.wildan.e_commerce.payload.ProductResponse;
import com.wildan.e_commerce.repositories.CategoryRepository;
import com.wildan.e_commerce.repositories.ProductRepository;
import com.wildan.e_commerce.utils.AuthUtil;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    private final CategoryRepository categoryRepository;

    private final ModelMapper modelMapper;

    private final AuthUtil authUtil;

    @Override
    public ProductResponse getAllProduct(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageDetails = PageRequest.of(pageNumber ,pageSize , sortByAndOrder);
        Page<Product> productPage = productRepository.findAll(pageDetails);

        List<Product> products = productPage.getContent();
        List<ProductDTO> productDTOS = products.stream()
                .map(product -> modelMapper.map(product , ProductDTO.class))
                .toList();

        if(products.isEmpty())
        {
            throw new APIException("No Products is in the database!");
        }

        return ProductResponse.builder()
                .content(productDTOS)
                .pageNumber(productPage.getNumber())
                .pageSize(productPage.getSize())
                .totalElements(productPage.getTotalElements())
                .totalpages(productPage.getTotalPages())
                .lastPage(productPage.isLast())
                .build();
    }

    @Override
    public ProductDTO createProduct(ProductDTO productDTO , Long categoryId) {
        Category categoryDB = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category" ,"categoryId" , categoryId));

        User user = authUtil.loggedInUser();


        List<Product> products = categoryDB.getProducts();

        boolean isProductNotAvailable = true;
        for(Product value : products){
            if(value.getName().equals(productDTO.getName())){
                isProductNotAvailable = false;
                break;
            }
        }

        if(isProductNotAvailable){
            Product product = modelMapper.map(productDTO , Product.class);
            product.setCategory(categoryDB);
            product.setImage("default.png");
            product.setSeller(user);
            productRepository.save(product);
            return modelMapper.map(product , ProductDTO.class);


        }
        else{
            throw new APIException("Product already exist!");
        }
    }

    @Override
    public ProductDTO deleteProduct(Long productId) {
        Product product =  productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product" ,"productId" , productId));

        ProductDTO productDTO = modelMapper.map(product , ProductDTO.class);
        productRepository.delete(product);

        return productDTO;


    }

    @Override
    public ProductDTO updateProduct(ProductDTO productDTO, Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product","productId" , productId));

        if(productDTO.getName() != null){
            product.setName(productDTO.getName());
        }

        if(productDTO.getPrice() != null){
            product.setPrice(productDTO.getPrice());
        }

        if(productDTO.getDescription() != null){
            product.setDescription(productDTO.getDescription());
        }

        if(productDTO.getImage() != null){
            product.setImage(productDTO.getImage());
        }

        if(productDTO.getQuantity() != null){
            product.setQuantity(productDTO.getQuantity());
        }

        Product savedProduct = productRepository.save(product);

        return modelMapper.map(savedProduct , ProductDTO.class);
    }

    @Override
    public ProductResponse getProductByKeyword(String keyword , Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageDetails = PageRequest.of(pageNumber ,pageSize , sortByAndOrder);
        Page<Product> productPage = productRepository.findByNameLikeIgnoreCase('%' + keyword + '%' , pageDetails);

        List<Product> products = productPage.getContent();
        List<ProductDTO> productDTOS = products.stream()
                .map(product -> modelMapper.map(product , ProductDTO.class))
                .toList();

        if(products.isEmpty())
        {
            throw new APIException("No Products is in the database!");
        }

        return ProductResponse.builder()
                .content(productDTOS)
                .pageNumber(productPage.getNumber())
                .pageSize(productPage.getSize())
                .totalElements(productPage.getTotalElements())
                .totalpages(productPage.getTotalPages())
                .lastPage(productPage.isLast())
                .build();
    }

    @Override
    public ProductResponse getProductByCategory(Long categoryId, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Categpry","categoryId" , categoryId));

        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageDetails = PageRequest.of(pageNumber ,pageSize , sortByAndOrder);
        Page<Product> productPage = productRepository.findByCategoryOrderByPriceAsc(category , pageDetails);

        List<Product> products = productPage.getContent();
        List<ProductDTO> productDTOS = products.stream()
                .map(product -> modelMapper.map(product , ProductDTO.class))
                .toList();

        if(products.isEmpty())
        {
            throw new APIException("No Products is in the database!");
        }

        return ProductResponse.builder()
                .content(productDTOS)
                .pageNumber(productPage.getNumber())
                .pageSize(productPage.getSize())
                .totalElements(productPage.getTotalElements())
                .totalpages(productPage.getTotalPages())
                .lastPage(productPage.isLast())
                .build();
    }
}
