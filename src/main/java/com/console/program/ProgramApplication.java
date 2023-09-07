package com.console.program;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.console.program.dto.requestDto.CouponApplicationOrdersInformation;
import com.console.program.dto.requestDto.GetAvailableCouponListRequestDto;
import com.console.program.dto.requestDto.GetParticularDateProductPriceRequestDto;
import com.console.program.dto.requestDto.PatchPriceProductRequestDto;
import com.console.program.dto.requestDto.PostCouponApplicationOrdersRequestDto;
import com.console.program.dto.requestDto.PostProductOrdersRequestDto;
import com.console.program.dto.requestDto.PostProductRequestDto;
import com.console.program.dto.requestDto.ProductOrdersInformation;
import com.console.program.dto.responseDto.CouponSummary;
import com.console.program.dto.responseDto.GetAvailableCouponListResponseDto;
import com.console.program.dto.responseDto.GetOrdersResponseDto;
import com.console.program.dto.responseDto.GetParticularDateProductPriceResponseDto;
import com.console.program.dto.responseDto.GetProductListResponseDto;
import com.console.program.dto.responseDto.GetProductPriceRecordResponseDto;
import com.console.program.dto.responseDto.GetProductResponseDto;
import com.console.program.dto.responseDto.GetRecentOrdersNumberResponseDto;
import com.console.program.dto.responseDto.GetTokenResponseDto;
import com.console.program.dto.responseDto.OrdersProduct;
import com.console.program.dto.responseDto.Product;
import com.console.program.dto.responseDto.ProductPriceRecordSummary;
import com.console.program.dto.responseDto.ResponseDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

@SpringBootApplication
public class ProgramApplication {

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		String type = "application/json";
		
		
		Loop1: while(true) {
				System.out.println("1. 마트 권한 발급 받기");
				System.out.println("2. 사용자 권한 발급 받기");
				System.out.println("3. 프로그램 종료");
				System.out.print("발급 받을 권한의 번호를 입력해주세요 : ");
				int inputNumber = scanner.nextInt();
				System.out.println("-------------------------------------------------------------------------------");


				if(inputNumber == 1) {
					GetTokenResponseDto getMartTokenResponseDto;
					String token = null;
					try {

						String getMartRoleTokenUrl = "http://localhost:4000/api/v1/auth/mart";
						
						HttpClient client = HttpClient.newHttpClient();
						HttpRequest request = HttpRequest.newBuilder()
							.uri(URI.create(getMartRoleTokenUrl))
							.GET()
							.build();

						HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
						String responseJson = response.body(); 

						Gson gson = new Gson();

						getMartTokenResponseDto = gson.fromJson(responseJson, GetTokenResponseDto.class);

						token = "Bearer " + getMartTokenResponseDto.getToken();
						
						
					} catch (Exception exception) {
						exception.printStackTrace();
					}



					while(true) {

						System.out.println("1. 상품 생성");
						System.out.println("2. 상품 가격 수정");
						System.out.println("3. 특정 시점 상품 가격 조회");
						System.out.println("4. 상품 삭제");
						System.out.println("5. 권한 발급 받기로 돌아가기");
						System.out.println("6. 프로그램 종료");
						System.out.print("번호를 입력해주세요 : ");
						int subInputNumber = scanner.nextInt();
						System.out.println("-------------------------------------------------------------------------------");


						if(subInputNumber == 1) {
							try {

								String postProduct = "http://localhost:4000/api/v1/product";



								List<PostProductRequestDto> productRequestDtoList = new ArrayList<>();

								PostProductRequestDto optionOne = new PostProductRequestDto("감자", 20000, 3000);
								productRequestDtoList.add(optionOne);

								PostProductRequestDto optionTwo = new PostProductRequestDto("배추", 38000, 0);
								productRequestDtoList.add(optionTwo);
								
								PostProductRequestDto optionThree = new PostProductRequestDto("감", 23000, 3000);
								productRequestDtoList.add(optionThree);
							

								ObjectMapper objectMapper = new ObjectMapper();

								for(int index = 0; index < productRequestDtoList.size(); index++){
									PostProductRequestDto postProductRequestDto = productRequestDtoList.get(index);

									String name = postProductRequestDto.getName();
									int price = postProductRequestDto.getPrice();
									int deliveryCharge = postProductRequestDto.getDeliveryCharge();

									System.out.println((index + 1) + "번 " + " 상품 이름: " + name + " 상품 가격: " + price + " 배달료 " + deliveryCharge);
								}
							

								System.out.print("상품 생성할 옵션 번호를 입력해주세요: ");
								int optionSelectNumber = scanner.nextInt();

								String requestBody = objectMapper
									.writerWithDefaultPrettyPrinter()
									.writeValueAsString(productRequestDtoList.get(optionSelectNumber-1));



								HttpClient client = HttpClient.newHttpClient();
								HttpRequest request = HttpRequest.newBuilder()
								.uri(URI.create(postProduct))
								.header("Authorization", token)
								.header("content-type", type)
								.POST(BodyPublishers.ofString(requestBody))
								.build();

								HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
								String responseJson = response.body(); 
								System.out.println(responseJson);
								System.out.println("-------------------------------------------------------------------------------");
								


								if(!(responseJson == null)) {

									String getProductList = "http://localhost:4000/api/v1/product/list";

									HttpClient clientTwo = HttpClient.newHttpClient();
									HttpRequest requestTwo = HttpRequest.newBuilder()
										.uri(URI.create(getProductList))
										.header("Authorization", token)
										.GET()
										.build();

									HttpResponse<String> responseTwo = clientTwo.send(requestTwo, HttpResponse.BodyHandlers.ofString());
									String responseJsonTwo = responseTwo.body(); 
									
									Gson gson = new Gson();

									GetProductListResponseDto getProductListResponseDto = gson.fromJson(responseJsonTwo, GetProductListResponseDto.class);

									List<Product> productList = getProductListResponseDto.getProductList();

									for(Product product: productList) {
										System.out.println(product);
									}
									System.out.println("-------------------------------------------------------------------------------");
								}

								

								
							} catch (Exception exception) {
								exception.printStackTrace();
							}

						}

						if(subInputNumber == 2) {

							try {

								String getProductList = "http://localhost:4000/api/v1/product/list";

								HttpClient client = HttpClient.newHttpClient();
								HttpRequest request = HttpRequest.newBuilder()
									.uri(URI.create(getProductList))
									.header("Authorization", token)
									.GET()
									.build();

								HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
								String responseJson = response.body(); 
								
								Gson gson = new Gson();

								GetProductListResponseDto getProductListResponseDto = gson.fromJson(responseJson, GetProductListResponseDto.class);

								List<Product> productList = getProductListResponseDto.getProductList();

								for(Product product: productList) {
									System.out.println(product);
								}
								System.out.println("-------------------------------------------------------------------------------");
								

								System.out.print("상품 목록 중에서 상품 가격을 수정하고 싶은 상품 번호(productNumber)를 입력해주세요: ");
								int inputProductNumber = scanner.nextInt();

								Loop2: while(true) {
									
									
									System.out.println("");
									System.out.print("수정할 상품 가격을 작성해주세요 (입력 예시: 24000) : ");
									int inputProductPrice = scanner.nextInt();

									for (Product product: productList) {
										int productNumber = product.getProductNumber();
										int productPrice = product.getProductPrice();

										if(inputProductNumber == productNumber) {
											
											if(inputProductPrice == productPrice) {
												System.out.println("똑같은 가격은 입력하실 수 없습니다.");
												System.out.println("다시 상품 가격을 작성해주길 바랍니다.");
												continue Loop2;
											}

										}
									}

									String patchProductPrice = "http://localhost:4000/api/v1/product/price";

									PatchPriceProductRequestDto requestDto = new PatchPriceProductRequestDto(inputProductNumber, inputProductPrice);

									ObjectMapper objectMapper = new ObjectMapper();

									String requestBody = objectMapper
										.writerWithDefaultPrettyPrinter()
										.writeValueAsString(requestDto);
						

									HttpClient client2 = HttpClient.newHttpClient();
									HttpRequest request2 = HttpRequest.newBuilder()
										.uri(URI.create(patchProductPrice))
										.method("PATCH", BodyPublishers.ofString(requestBody))
										.header("Authorization", token)
										.header("content-type", type)
										.build();

									HttpResponse<String> response2 = client2.send(request2, HttpResponse.BodyHandlers.ofString());
									String responseJson2 = response2.body(); 
									
									System.out.println(responseJson2);




									String getProdcut = "http://localhost:4000/api/v1/product/" + inputProductNumber;

									HttpClient client3 = HttpClient.newHttpClient();
									HttpRequest request3 = HttpRequest.newBuilder()
										.uri(URI.create(getProdcut))
										.header("Authorization", token)
										.GET()
										.build();


									HttpResponse<String> response3 = client3.send(request3, HttpResponse.BodyHandlers.ofString());
									String responseJson3 = response3.body(); 

									Gson gson2 = new Gson();

									GetProductResponseDto getProductResponseDto = gson2.fromJson(responseJson3, GetProductResponseDto.class);

									System.out.println(getProductResponseDto.toString());
									System.out.println("-------------------------------------------------------------------------------");
								
									
									break;
								}


							} catch (Exception exception) {
								exception.printStackTrace();
							}
						}



						if(subInputNumber == 3) {
							try {
								
								String getProductList = "http://localhost:4000/api/v1/product/list";

								HttpClient client = HttpClient.newHttpClient();
								HttpRequest request = HttpRequest.newBuilder()
									.uri(URI.create(getProductList))
									.header("Authorization", token)
									.GET()
									.build();

								HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
								String responseJson = response.body(); 
								
								Gson gson = new Gson();

								GetProductListResponseDto getProductListResponseDto = gson.fromJson(responseJson, GetProductListResponseDto.class);

								List<Product> productList = getProductListResponseDto.getProductList();

								for(Product product: productList) {
									System.out.println(product);
								}
								System.out.println("-------------------------------------------------------------------------------");
								

								System.out.print("상품 목록 중에서 특정 시점 상품 가격을 조회할 상품 번호(productNumber)를 입력해주세요: ");
								int inputProductNumber = scanner.nextInt();



								String getProductPriceRecordList = "http://localhost:4000/api/v1/product/price-record/" + inputProductNumber;
								
								HttpClient client2 = HttpClient.newHttpClient();
								HttpRequest request2 = HttpRequest.newBuilder()
									.uri(URI.create(getProductPriceRecordList))
									.header("Authorization", token)
									.header("content-type", type)
									.GET()
									.build();

								HttpResponse<String> response2 = client2.send(request2, HttpResponse.BodyHandlers.ofString());
								String responseJson2 = response2.body(); 
								
								Gson gson2 = new Gson();

								GetProductPriceRecordResponseDto getProductPriceRecordResponseDto = gson2.fromJson(responseJson2, GetProductPriceRecordResponseDto.class); 

								List<ProductPriceRecordSummary> priceRecordSummaryList = getProductPriceRecordResponseDto.getProductPriceRecordList();

								Collections.sort(priceRecordSummaryList);

								for(int index = 0; index < priceRecordSummaryList.size(); index++) {
									ProductPriceRecordSummary productPriceRecordSummary = priceRecordSummaryList.get(index);
									String modifyDate = productPriceRecordSummary.getModifyDate();

									System.out.println((index + 1) + "번 " + " 수정 날짜: " + modifyDate);
								}

								System.out.print("상품 수정 이력 목록 중에서 조회하고 싶은 날짜의 번호를 입력해주세요: ");
								int inputModifyDateNumber = scanner.nextInt();
								
								String inputModifyDate = priceRecordSummaryList.get(inputModifyDateNumber-1).getModifyDate();
								
								
								String getParticularDateProductPrice = "http://localhost:4000/api/v1/product/price";

								GetParticularDateProductPriceRequestDto requestDto = new GetParticularDateProductPriceRequestDto(inputModifyDate, inputProductNumber);

								ObjectMapper objectMapper = new ObjectMapper();

									String requestBody = objectMapper
										.writerWithDefaultPrettyPrinter()
										.writeValueAsString(requestDto);

								HttpClient client3 = HttpClient.newHttpClient();
								HttpRequest request3 = HttpRequest.newBuilder()
									.uri(URI.create(getParticularDateProductPrice))
									.method("GET", BodyPublishers.ofString(requestBody))
									.header("Authorization", token)
									.header("content-type", type)
									.build();

								HttpResponse<String> response3 = client3.send(request3, HttpResponse.BodyHandlers.ofString());
								String responseJson3 = response3.body(); 

								System.out.println(responseJson3);

								Gson gson3 = new Gson();

								GetParticularDateProductPriceResponseDto getParticularDateProductPriceResponseDto  = gson3.fromJson(responseJson3, GetParticularDateProductPriceResponseDto.class); 

								System.out.println(getParticularDateProductPriceResponseDto.getPrice() + "원");

							} catch (Exception exception) {
								exception.printStackTrace();
							}
						}

						if(subInputNumber == 4) {

							try {

								while(true) {

									String getProductList = "http://localhost:4000/api/v1/product/list";

									HttpClient client = HttpClient.newHttpClient();
									HttpRequest request = HttpRequest.newBuilder()
										.uri(URI.create(getProductList))
										.header("Authorization", token)
										.GET()
										.build();

									HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
									String responseJson = response.body(); 
									
									Gson gson = new Gson();

									GetProductListResponseDto getProductListResponseDto = gson.fromJson(responseJson, GetProductListResponseDto.class);

									List<Product> productList = getProductListResponseDto.getProductList();

									for(Product product: productList) {
										System.out.println(product);
									}
									System.out.println("-------------------------------------------------------------------------------");
									

									System.out.print("상품 목록 중에서 삭제할 상품 번호(productNumber)를 입력해주세요: ");
									int inputProductNumber = scanner.nextInt();
									System.out.println("-------------------------------------------------------------------------------");


									String deleteProduct = "http://localhost:4000/api/v1/product/"+inputProductNumber;


									HttpClient client2 = HttpClient.newHttpClient();
									HttpRequest request2 = HttpRequest.newBuilder()
										.uri(URI.create(deleteProduct))
										.header("Authorization", token)
										.DELETE()
										.build();

									HttpResponse<String> response2 = client2.send(request2, HttpResponse.BodyHandlers.ofString());
									String responseJson2 = response2.body(); 

									Gson gson2 = new Gson();

									ResponseDto responseDto  = gson2.fromJson(responseJson2, ResponseDto.class);
									String code = responseDto.getCode();

									if(code.equals("NEPN")) {
										System.out.println("존재하지 않는 상품 번호를 입력하셨습니다.");
										System.out.println("다시 상품 번호를 입력해주시길 바랍니다.");
										System.out.println("-------------------------------------------------------------------------------");
										continue;
									}

									System.out.println(responseJson2);




									HttpClient client3 = HttpClient.newHttpClient();
									HttpRequest request3 = HttpRequest.newBuilder()
										.uri(URI.create(getProductList))
										.header("Authorization", token)
										.GET()
										.build();

									HttpResponse<String> response3 = client3.send(request3, HttpResponse.BodyHandlers.ofString());
									String responseJson3 = response3.body(); 
									
									Gson gson3 = new Gson();

									GetProductListResponseDto getProductListResponseDto2 = gson3.fromJson(responseJson3, GetProductListResponseDto.class);

									List<Product> productList2 = getProductListResponseDto2.getProductList();

									for(Product product: productList2) {
										System.out.println(product);
									}
									System.out.println("-------------------------------------------------------------------------------");
									break;
								}


							} catch (Exception exception) {
								exception.printStackTrace();
							}

						}

						if(subInputNumber == 5) break;

						if(subInputNumber == 6) break Loop1;
					}

					

					
					

				}

				if(inputNumber == 2) {

					GetTokenResponseDto getUserTokenResponseDto;
					String token = null;
					String getUserRoleTokenUrl = "http://localhost:4000/api/v1/auth/user/jhe2426";

					try {

						
						
						HttpClient client = HttpClient.newHttpClient();
						HttpRequest request = HttpRequest.newBuilder()
							.uri(URI.create(getUserRoleTokenUrl))
							.GET()
							.build();

						HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
						String responseJson = response.body(); 

						Gson gson = new Gson();

						getUserTokenResponseDto = gson.fromJson(responseJson, GetTokenResponseDto.class);

						token = "Bearer " + getUserTokenResponseDto.getToken();
						
						
					} catch (Exception exception) {
						exception.printStackTrace();
					}


					while(true) {

						System.out.println("1. 상품 주문");
						System.out.println("2. 쿠폰을 적용하여 상품 주문");
						System.out.println("3. 권한 발급 받기로 돌아가기");
						System.out.println("4. 프로그램 종료");
						System.out.print("번호를 입력해주세요: ");
						int subInputNumber = scanner.nextInt();
						System.out.println("-------------------------------------------------------------------------------");

						if(subInputNumber == 1) {

		
							try {

								
								Loop2: while(true) {
									try {

										Scanner stringScanner = new Scanner(System.in);

										String getProductList = "http://localhost:4000/api/v1/product/list";

										HttpClient client = HttpClient.newHttpClient();
										HttpRequest request = HttpRequest.newBuilder()
											.uri(URI.create(getProductList))
											.header("Authorization", token)
											.GET()
											.build();

										HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
										String responseJson = response.body(); 
										
										Gson gson = new Gson();

										GetProductListResponseDto getProductListResponseDto = gson.fromJson(responseJson, GetProductListResponseDto.class);

										List<Product> productList = getProductListResponseDto.getProductList();

										for(Product product: productList) {
											System.out.println(product);
										}

										System.out.println("-------------------------------------------------------------------------------");
									
										List<Integer> inputProductNumberList = new ArrayList<>();
										List<Integer> inputProductQuantityList = new ArrayList<>();		

										
										System.out.print("상품 목록 중에서 주문할 상품 번호를 입력해주세요 (입력 예시: 2,3,4,5 ) : ");
										String inputProductNumber = stringScanner.next();
										String[] inputProductNumberArray = inputProductNumber.split(",");

											for(int index = 0; index < inputProductNumberArray.length; index++) {
												String stringProductNumber = inputProductNumberArray[index];
												inputProductNumberList.add(Integer.parseInt(stringProductNumber));
											}

										
										
										System.out.println(inputProductNumber + " 입력하신 상품 번호입니다.");
										System.out.print("위의 입력하신 상품 번호에 맞게 구매하실 상품의 개수를 입력해주세요. (입력 예시: 1,3,4) : ");
										String inputProductQuantity = stringScanner.next();
										System.out.println("-------------------------------------------------------------------------------");
										String[] inputProductQuantityArray = inputProductQuantity.split(",");

										for(int index = 0; index < inputProductQuantityArray.length; index++) {
											String stringProductQuantity = inputProductQuantityArray[index];
											inputProductQuantityList.add(Integer.parseInt(stringProductQuantity));
										}

										String postOrders = "http://localhost:4000/api/v1/orders";


										if(!(inputProductNumberList.size() == inputProductQuantityList.size())) {
											System.out.println("입력하신 상품 번호의 개수와 똑같은 개수로 상품 개수를 입력해주세요");
											System.out.println("-------------------------------------------------------------------------------");
											continue Loop2;
										}

										ObjectMapper objectMapper = new ObjectMapper();

										List<ProductOrdersInformation> productOrdersInformationList = new ArrayList<>();

										for(int index = 0; index < inputProductNumberList.size(); index++) {
											int inputProdcutNumber = inputProductNumberList.get(index);
											int inputProductQuantityNumber = inputProductQuantityList.get(index);
											ProductOrdersInformation productOrdersInformation = new ProductOrdersInformation(inputProdcutNumber, inputProductQuantityNumber);
											productOrdersInformationList.add(productOrdersInformation);
										}

										
										PostProductOrdersRequestDto productOrdersRequestDto = new PostProductOrdersRequestDto(productOrdersInformationList);

										String requestBody = objectMapper
										 	.writerWithDefaultPrettyPrinter()
										 	.writeValueAsString(productOrdersRequestDto);
							

										HttpClient client2 = HttpClient.newHttpClient();
										HttpRequest request2 = HttpRequest.newBuilder()
											.uri(URI.create(postOrders))
											.header("Authorization", token)
											.header("content-type", type)
											.POST(BodyPublishers.ofString(requestBody))
											.build();

										HttpResponse<String> response2 = client2.send(request2, HttpResponse.BodyHandlers.ofString());
										String responseJson2 = response2.body(); 
										
										Gson gson2 = new Gson();

										ResponseDto responseDto = gson2.fromJson(responseJson2, ResponseDto.class);

										String code = responseDto.getCode();

										if(code.equals("NEPN")) {
											System.out.println("존재하지 않는 상품 번호입니다.");
											continue Loop2;
										}

										System.out.println(responseJson2);


										break;
									} catch (Exception e) {
										e.printStackTrace();
										System.err.println("주문할 상품 번호를 1,2,3 이런 형식으로 작성해주시길 바랍니다.");
										continue Loop2;
									}

								}



								String getRecentOrdersNumber = "http://localhost:4000/api/v1/orders/recent-orders-number";

								HttpClient client = HttpClient.newHttpClient();
								HttpRequest request = HttpRequest.newBuilder()
									.uri(URI.create(getRecentOrdersNumber))
									.header("Authorization", token)
									.GET()
									.build();

								HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
								String responseJson = response.body(); 
								
								Gson gson = new Gson();

								GetRecentOrdersNumberResponseDto getRecentOrdersNumberResponseDto = gson.fromJson(responseJson, GetRecentOrdersNumberResponseDto.class);

										
								int recentOrdersNubmer = getRecentOrdersNumberResponseDto.getRecentOrdersNumber();





								String getOrders = "http://localhost:4000/api/v1/orders/" + recentOrdersNubmer;

								HttpClient client2 = HttpClient.newHttpClient();
								HttpRequest request2 = HttpRequest.newBuilder()
									.uri(URI.create(getOrders))
									.header("Authorization", token)
									.GET()
									.build();

								HttpResponse<String> response2 = client2.send(request2, HttpResponse.BodyHandlers.ofString());
								String responseJson2 = response2.body(); 
								
								Gson gson2 = new Gson();

								GetOrdersResponseDto getOrdersResponseDto = gson2.fromJson(responseJson2, GetOrdersResponseDto.class);

								System.out.print("ordersNumber = " + getOrdersResponseDto.getOrdersNumber());
								System.out.print(" userNumber = " + getOrdersResponseDto.getUserNumber());
								System.out.print(" totalOrderCount = " + getOrdersResponseDto.getTotalOrderCount());
								System.out.print(" orderPrice = " + getOrdersResponseDto.getOrderPrice());
								System.out.print(" couponPkNumber = " + getOrdersResponseDto.getCouponPkNumber());
								System.out.println(" deliveryCharge = " +  getOrdersResponseDto.getDeliveryCharge());
								System.out.println(" ordersProdcutList = ");

								List<OrdersProduct> ordersProductList = getOrdersResponseDto.getOrdersProdcutList();

								for(OrdersProduct ordersProduct: ordersProductList){
									int ordersProductNumber = ordersProduct.getOrdersProductNumber();
									int productNumber = ordersProduct.getProductNumber();
									int productQuantity = ordersProduct.getProductQuantity();
									System.out.println("ordersProductNumber = " + ordersProductNumber + " productNumber = " + productNumber + " productQuantity = " + productQuantity);
								}
								


							} catch (Exception exception) {
								exception.printStackTrace();
						}
					}

					if(subInputNumber == 2) {


						try {
							
							Loop2: while(true) {
									try {

										Scanner stringScanner = new Scanner(System.in);

										String getProductList = "http://localhost:4000/api/v1/product/list";

										HttpClient client = HttpClient.newHttpClient();
										HttpRequest request = HttpRequest.newBuilder()
											.uri(URI.create(getProductList))
											.header("Authorization", token)
											.GET()
											.build();

										HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
										String responseJson = response.body(); 
										
										Gson gson = new Gson();

										GetProductListResponseDto getProductListResponseDto = gson.fromJson(responseJson, GetProductListResponseDto.class);

										List<Product> productList = getProductListResponseDto.getProductList();

										for(Product product: productList) {
											System.out.println(product);
										}

										System.out.println("-------------------------------------------------------------------------------");
									
										List<Integer> inputProductNumberList = new ArrayList<>();
										List<Integer> inputProductQuantityList = new ArrayList<>();		

										
										System.out.print("상품 목록 중에서 주문할 상품 번호를 입력해주세요 (입력 예시: 2,3,4,5 ) : ");
										String inputProductNumber = stringScanner.next();
										String[] inputProductNumberArray = inputProductNumber.split(",");

										for(int index = 0; index < inputProductNumberArray.length; index++) {
											String stringProductNumber = inputProductNumberArray[index];
											inputProductNumberList.add(Integer.parseInt(stringProductNumber));
										}

										
										
										System.out.println(inputProductNumber + " 입력하신 상품 번호입니다.");
										System.out.print("위의 입력하신 상품 번호에 맞게 구매하실 상품의 개수를 입력해주세요. (입력 예시: 1,3,4) : ");
										String inputProductQuantity = stringScanner.next();
										System.out.println("-------------------------------------------------------------------------------");

										String[] inputProductQuantityArray = inputProductQuantity.split(",");

										for(int index = 0; index < inputProductQuantityArray.length; index++) {
											String stringProductQuantity = inputProductQuantityArray[index];
											inputProductQuantityList.add(Integer.parseInt(stringProductQuantity));
										}
										
										

										

										String postOrders = "http://localhost:4000/api/v1/orders";


										if(!(inputProductNumberList.size() == inputProductQuantityList.size())) {
											System.out.println("입력하신 상품 번호의 개수와 똑같은 개수로 상품 개수를 입력해주세요");
											continue Loop2;
										}

										ObjectMapper objectMapper = new ObjectMapper();

										List<ProductOrdersInformation> productOrdersInformationList = new ArrayList<>();

										for(int index = 0; index < inputProductNumberList.size(); index++) {
											int inputProdcutNumber = inputProductNumberList.get(index);
											int inputProductQuantityNumber = inputProductQuantityList.get(index);
											ProductOrdersInformation productOrdersInformation = new ProductOrdersInformation(inputProdcutNumber, inputProductQuantityNumber);
											productOrdersInformationList.add(productOrdersInformation);
										}

										




										String getAvailableCouponList = "http://localhost:4000/api/v1/orders/available-coupon/list";

										GetAvailableCouponListRequestDto getAvailableCouponListRequestDto = new GetAvailableCouponListRequestDto(inputProductNumberList);
						

										

										String requestBody = objectMapper
											.writerWithDefaultPrettyPrinter()
											.writeValueAsString(getAvailableCouponListRequestDto);

										HttpClient client2 = HttpClient.newHttpClient();
										HttpRequest request2 = HttpRequest.newBuilder()
											.uri(URI.create(getAvailableCouponList))
											.method("GET", BodyPublishers.ofString(requestBody))
											.header("Authorization", token)
											.header("content-type", type)
											.build();

										HttpResponse<String> response2 = client2.send(request2, HttpResponse.BodyHandlers.ofString());
										String responseJson2 = response2.body(); 


										Gson gson2 = new Gson();

										GetAvailableCouponListResponseDto getAvailableCouponListResponseDto2  = gson2.fromJson(responseJson2, GetAvailableCouponListResponseDto.class); 

										String code = getAvailableCouponListResponseDto2.getCode();

										if(code.equals("NEPN")) {
											System.out.println("존재하지 않는 상품 번호를 입력하셨습니다.");
											System.out.println("존재하는 상품 번호를 정확하게 입력해주시길 바랍니다.");
											System.out.println("-------------------------------------------------------------------------------");
											continue;
										}
										System.out.println(code);

										if(code.equals("NECN")) {
											System.out.println("존재하지 않는 쿠폰 번호입니다.");
											System.out.println("-------------------------------------------------------------------------------");
											continue;
										}

										if(code.equals("NEUC")) {
											System.out.println("사용자가 사용할 수 있는 쿠폰이 존재하지 않습니다.");
											System.out.println("-------------------------------------------------------------------------------");
											continue;
										}

										List<CouponSummary> couponSummarieList = getAvailableCouponListResponseDto2.getCouponList();


										for(CouponSummary couponSummary: couponSummarieList) {
											System.out.println(couponSummary.toString());
										}

										int inputCouponNumber = 0;
										System.out.print("적용하실 쿠폰 번호(couponNumber) 한 개를 입력해주세요 (입력 예시: 1) : ");
										inputCouponNumber = scanner.nextInt();




										String postCouponApplicationOrders = "http://localhost:4000/api/v1/orders/application-coupon";

										List<CouponApplicationOrdersInformation> couponApplicationOrdersInformationList = new ArrayList<>();

										for(int index = 0; index < inputProductNumberList.size(); index++) {
											int inputProdcutNumber = inputProductNumberList.get(index);
											int inputProductQuantityNumber = inputProductQuantityList.get(index);
											CouponApplicationOrdersInformation couponApplicationOrdersInformation = new CouponApplicationOrdersInformation(inputProdcutNumber, inputProductQuantityNumber);
											couponApplicationOrdersInformationList.add(couponApplicationOrdersInformation);
										}

										PostCouponApplicationOrdersRequestDto postCouponApplicationOrdersRequestDto = new PostCouponApplicationOrdersRequestDto(couponApplicationOrdersInformationList, inputCouponNumber);

										String requestBody3 = objectMapper
										 	.writerWithDefaultPrettyPrinter()
										 	.writeValueAsString(postCouponApplicationOrdersRequestDto);
							

										HttpClient client3 = HttpClient.newHttpClient();
										HttpRequest request3 = HttpRequest.newBuilder()
											.uri(URI.create(postCouponApplicationOrders))
											.header("Authorization", token)
											.header("content-type", type)
											.POST(BodyPublishers.ofString(requestBody3))
											.build();

										HttpResponse<String> response3 = client3.send(request3, HttpResponse.BodyHandlers.ofString());
										String responseJson3 = response3.body(); 
										
										Gson gson3 = new Gson();

										ResponseDto responseDto3 = gson3.fromJson(responseJson3, ResponseDto.class);

										String code2 = responseDto3.getCode();

										System.out.println(code2);

										if(code.equals("NEPN")) {
											System.out.println("존재하지 않는 상품 번호입니다.");
											System.out.println("-------------------------------------------------------------------------------");
											continue Loop2;
										}

										System.out.println(responseJson3);


										break;
									} catch (Exception e) {
										System.err.println("주문할 상품 번호를 1,2,3 이런 형식으로 작성해주시길 바랍니다.");
										System.out.println("-------------------------------------------------------------------------------");
										continue Loop2;
									}

								}


								String getRecentOrdersNumber = "http://localhost:4000/api/v1/orders/recent-orders-number";

								HttpClient client = HttpClient.newHttpClient();
								HttpRequest request = HttpRequest.newBuilder()
									.uri(URI.create(getRecentOrdersNumber))
									.header("Authorization", token)
									.GET()
									.build();

								HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
								String responseJson = response.body(); 
								
								Gson gson = new Gson();

								GetRecentOrdersNumberResponseDto getRecentOrdersNumberResponseDto = gson.fromJson(responseJson, GetRecentOrdersNumberResponseDto.class);

										
								int recentOrdersNubmer = getRecentOrdersNumberResponseDto.getRecentOrdersNumber();





								String getOrders = "http://localhost:4000/api/v1/orders/" + recentOrdersNubmer;

								HttpClient client2 = HttpClient.newHttpClient();
								HttpRequest request2 = HttpRequest.newBuilder()
									.uri(URI.create(getOrders))
									.header("Authorization", token)
									.GET()
									.build();

								HttpResponse<String> response2 = client2.send(request2, HttpResponse.BodyHandlers.ofString());
								String responseJson2 = response2.body(); 
								
								Gson gson2 = new Gson();

								GetOrdersResponseDto getOrdersResponseDto = gson2.fromJson(responseJson2, GetOrdersResponseDto.class);

								System.out.print("ordersNumber = " + getOrdersResponseDto.getOrdersNumber());
								System.out.print(" userNumber = " + getOrdersResponseDto.getUserNumber());
								System.out.print(" totalOrderCount = " + getOrdersResponseDto.getTotalOrderCount());
								System.out.print(" orderPrice = " + getOrdersResponseDto.getOrderPrice());
								System.out.print(" couponPkNumber = " + getOrdersResponseDto.getCouponPkNumber());
								System.out.println(" deliveryCharge = " +  getOrdersResponseDto.getDeliveryCharge());
								System.out.println(" ordersProdcutList = ");

								List<OrdersProduct> ordersProductList = getOrdersResponseDto.getOrdersProdcutList();

								for(OrdersProduct ordersProduct: ordersProductList){
									int ordersProductNumber = ordersProduct.getOrdersProductNumber();
									int productNumber = ordersProduct.getProductNumber();
									int productQuantity = ordersProduct.getProductQuantity();
									System.out.println("ordersProductNumber = " + ordersProductNumber + " productNumber = " + productNumber + " productQuantity = " + productQuantity);
								}



								


						} catch (Exception exception) {
							exception.printStackTrace();
						}


						

					}

					if(subInputNumber == 3) break;

					if(subInputNumber == 4) break Loop1;

					}



					
				}

				
				if(inputNumber == 3) break;
				
			}
	}

}
