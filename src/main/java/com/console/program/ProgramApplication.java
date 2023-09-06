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

import com.console.program.dto.requestDto.GetParticularDateProductPriceRequestDto;
import com.console.program.dto.requestDto.PatchPriceProductRequestDto;
import com.console.program.dto.requestDto.PostProductRequestDto;
import com.console.program.dto.responseDto.GetParticularDateProductPriceResponseDto;
import com.console.program.dto.responseDto.GetProductListResponseDto;
import com.console.program.dto.responseDto.GetProductPriceRecordResponseDto;
import com.console.program.dto.responseDto.GetProductResponseDto;
import com.console.program.dto.responseDto.GetTokenResponseDto;
import com.console.program.dto.responseDto.Product;
import com.console.program.dto.responseDto.ProductPriceRecordSummary;
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
						System.err.println(exception.toString());
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
								
								PostProductRequestDto optionThree = new PostProductRequestDto("감", 23000, 30000);
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
								System.err.println(exception.toString());
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
								System.err.println(exception.toString());
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


							} catch (Exception exception) {
								exception.printStackTrace();
							}

						}

						if(subInputNumber == 5) break;

						if(subInputNumber == 6) break Loop1;
					}

					

					
					

				}

				if(inputNumber == 2) {
					System.out.println("1. 상품 주문");
					System.out.println("2. 쿠폰을 적용하여 상품 주문");
					System.out.println("3. 권한 발급 받기로 돌아가기");
					System.out.println("4. 프로그램 종료");
					System.out.print("번호를 입력해주세요: ");
					int subInputNumber = scanner.nextInt();
					System.out.println("-------------------------------------------------------------------------------");

					if(subInputNumber == 1) {}

					if(subInputNumber == 2) {}

					if(subInputNumber == 3) continue;

					if(subInputNumber == 4) break;
				}

				
				if(inputNumber == 3) break;
				
			}
	}

}
