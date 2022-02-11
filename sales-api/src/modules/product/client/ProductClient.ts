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
    bearerToken: string,
    transactionid: string
  ): Promise<Boolean> {
    try {
      const headers = { authorization: bearerToken, transactionid };
      console.info(
        `Sending request to Product API with data: ${JSON.stringify(
          products
        )} and transactionid ${transactionid}`
      );
      let response = false;
      await axios
        .post<CheckResponse>(`${PRODUCT_API_URL}/check-stock`, products, {
          headers,
        })
        .then((res) => {
          console.info(
            `Success response from Product API with data: ${JSON.stringify(
              res.data
            )} and transactionid ${transactionid}`
          );
          response = true;
        })
        .catch((err) => {
          console.info(
            `Error response from Product API with data: ${JSON.stringify(
              err.response.data
            )} and transactionid ${transactionid}`
          );
          response = false;
        });
      return response;
    } catch (err) {
      console.info(
        `Error response from Product API with data: ${JSON.stringify(
          err.message
        )} and transactionid ${transactionid}`
      );
      return false;
    }
  }
}

export default new ProductClient();
