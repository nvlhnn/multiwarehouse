gcloud auth login

docker tag com.ecommerce.app/order.service:$1 asia-southeast2-docker.pkg.dev/com-ecommerce-app/com-ecommerce-app-repository/order.service:$1
docker tag com.ecommerce.app/payment.service:$1 asia-southeast2-docker.pkg.dev/com-ecommerce-app/com-ecommerce-app-repository/payment.service:$1
docker tag com.ecommerce.app/customer.service:$1 asia-southeast2-docker.pkg.dev/com-ecommerce-app/com-ecommerce-app-repository/customer.service:$1

docker push asia-southeast2-docker.pkg.dev/com-ecommerce-app/com-ecommerce-app-repository/order.service:$1
docker push asia-southeast2-docker.pkg.dev/com-ecommerce-app/com-ecommerce-app-repository/payment.service:$1
docker push asia-southeast2-docker.pkg.dev/com-ecommerce-app/com-ecommerce-app-repository/customer.service:$1

