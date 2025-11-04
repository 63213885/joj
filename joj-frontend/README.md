# joj-frontend

## 根据后台生成代码
```shell
openapi --input http://localhost:8101/api/user/v2/api-docs --output ./generated/user --client axios
openapi --input http://localhost:8101/api/question/v2/api-docs --output ./generated/question --client axios
```

## Project setup
```
npm install
```

### Compiles and hot-reloads for development
```
npm run serve
```

### Compiles and minifies for production
```
npm run build
```

### Lints and fixes files
```
npm run lint
```

### Customize configuration
See [Configuration Reference](https://cli.vuejs.org/config/).
