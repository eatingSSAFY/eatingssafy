import { Helmet } from 'react-helmet-async';

import EcommerceProductView from 'src/sections/_ecommerce-dev/view/ecommerce-product-view';

// ----------------------------------------------------------------------

export default function EcommerceProductPage() {
  return (
    <>
      <Helmet>
        <title> E-commerce: Product</title>
      </Helmet>

      <EcommerceProductView />
    </>
  );
}
