import axios from 'axios';

import { PRODUCT_API_URL } from '../../../config/constants/secrets';
import { IProduct } from '../../sales/model/Order';

interface Products {
  products: IProduct[];
}

interface CheckResponse {
  status: number;
  message: string;
}

class ProductClient {
  async checkProductStock(
    products: Products,
    bearerToken: string
  ): Promise<Boolean> {
    try {
      const headers = { authorization: bearerToken };
      console.info(
        `Sending request to Product API with data: ${JSON.stringify(products)}`
      );
      let response = false;
      await axios
        .post<CheckResponse>(`${PRODUCT_API_URL}/check-stock`, products, {
          headers,
        })
        .then((res) => {
          response = true;
        })
        .catch((err) => {
          console.error(err.response.data);
          response = false;
        });
      return response;
    } catch (err) {
      console.error(err.message);
      return false;
    }
  }
}

export default new ProductClient();
