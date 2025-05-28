package ecommerce.middleware.product.dto;

public class ProductImageDTO {
    private Long id;
    private byte[] imageData;
    private boolean isLogo;
    private Long productId;
    private Long supplierId;

    public ProductImageDTO() {
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public byte[] getImageData() {
        return imageData;
    }
    public void setImageData(byte[] imageData) {
        this.imageData = imageData;
    }
    public boolean isLogo() {
        return isLogo;
    }
    public void setLogo(boolean isLogo) {
        this.isLogo = isLogo;
    }
    public Long getProductId() {
        return productId;
    }
    public void setProductId(Long productId) {
        this.productId = productId;
    }
    public Long getSupplierId() {
        return supplierId;
    }
    public void setSupplierId(Long supplierId) {
        this.supplierId = supplierId;
    }

    
}