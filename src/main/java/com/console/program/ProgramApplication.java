package com.console.program;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
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
		
		
		while(true) {
            System.out.println("1. 마트 권한 발급 받기");
            System.out.println("2. 사용자 권한 발급 받기");
            System.out.println("3. 프로그램 종료");
            System.out.print("발급 받을 권한의 번호를 입력해주세요 : ");
            int inputNumber = scanner.nextInt();
            System.out.println("---------------------------------------------");


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
					String json = response.body(); 

					Gson gson = new Gson();

					getMartTokenResponseDto = gson.fromJson(json, GetTokenResponseDto.class);

					token = "Bearer " + getMartTokenResponseDto.getToken();
					
					
				} catch (Exception exception) {
					System.err.println(exception.toString());
        		}


                System.out.println("1. 상품 생성");
                System.out.println("2. 상품 가격 수정");
                System.out.println("3. 특정 시점 상품 가격 조회");
                System.out.println("4. 상품 삭제");
                System.out.println("5. 권한 발급 받기로 돌아가기");
                System.out.println("6. 프로그램 종료");
                System.out.print("번호를 입력해주세요 : ");
                int subInputNumber = scanner.nextInt();
                System.out.println("---------------------------------------------");


                if(subInputNumber == 1) {
					try {

						String postProduct = "http://localhost:4000/api/v1/product";

						ObjectMapper objectMapper = new ObjectMapper();

						PostProductRequestDto dto = new PostProductRequestDto("감자", 10000, 0);
						String requestBody = objectMapper
							.writerWithDefaultPrettyPrinter()
							.writeValueAsString(dto);



						HttpClient client = HttpClient.newHttpClient();
						HttpRequest request = HttpRequest.newBuilder()
						.uri(URI.create(postProduct))
						.header("Authorization", token)
						.header("content-type", type)
						.POST(BodyPublishers.ofString(requestBody))
						.build();

						HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
						String json = response.body(); 
						System.out.println(json);
						
					} catch (Exception exception) {
						System.err.println(exception.toString());
					}

					try {
						String getProductList = "http://localhost:4000/api/v1/product/list";

						HttpClient client = HttpClient.newHttpClient();
						HttpRequest request = HttpRequest.newBuilder()
							.uri(URI.create(getProductList))
							.header("Authorization", token)
							.GET()
							.build();

						HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
						String json = response.body(); 
						
						Gson gson = new Gson();

						GetProductListResponseDto getProductListResponseDto = gson.fromJson(json, GetProductListResponseDto.class);

						List<Product> productList = getProductListResponseDto.getProductList();

						for(Product product: productList) {
							System.out.println(product);
						}

					} catch (Exception exception) {
						System.err.println(exception.toString());
					}
				}

                if(subInputNumber == 2) {}

                if(subInputNumber == 3) {}

                if(subInputNumber == 4) {}

                if(subInputNumber == 5) continue;

                if(subInputNumber == 6) break;
            }

            if(inputNumber == 2) {
                System.out.println("1. 상품 주문");
                System.out.println("2. 쿠폰을 적용하여 상품 주문");
                System.out.println("3. 권한 발급 받기로 돌아가기");
                System.out.println("4. 프로그램 종료");
                int subInputNumber = scanner.nextInt();
                System.out.println("---------------------------------------------");

                if(subInputNumber == 1) {}

                if(subInputNumber == 2) {}

                if(subInputNumber == 3) continue;

                if(subInputNumber == 4) break;
            }

            
            if(inputNumber == 3) break;
            
        }
	}

}
