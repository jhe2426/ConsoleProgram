package com.console.program;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.console.program.dto.requestDto.PostProductRequestDto;
import com.console.program.dto.responseDto.GetProductListResponseDto;
import com.console.program.dto.responseDto.GetTokenResponseDto;
import com.console.program.dto.responseDto.Product;
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

						if(subInputNumber == 2) {}

						if(subInputNumber == 3) {}

						if(subInputNumber == 4) {}

						if(subInputNumber == 5) break;

						if(subInputNumber == 6) break Loop1;
					}

					

					
					

				}

				if(inputNumber == 2) {
					System.out.println("1. 상품 주문");
					System.out.println("2. 쿠폰을 적용하여 상품 주문");
					System.out.println("3. 권한 발급 받기로 돌아가기");
					System.out.println("4. 프로그램 종료");
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
