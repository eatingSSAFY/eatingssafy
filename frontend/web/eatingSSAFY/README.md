# VScode setting

1. Local Environment variables setting
    - Create .env in the root directory
        
        ```jsx
        NEXT_PUBLIC_API_SERVER_URL=YOUR SERVER URL
        ```
        
    - next.config.js
        
        ```jsx
        module.exports = {
          output: 'standalone',
          trailingSlash: true,
          compiler: {
            // ssr and displayName are configured by default
            styledComponents: true,
          },
          modularizeImports: {
            '@mui/icons-material': {
              transform: '@mui/icons-material/{{member}}',
            },
            '@mui/material': {
              transform: '@mui/material/{{member}}',
            },
            '@mui/lab': {
              transform: '@mui/lab/{{member}}',
            },
          },
          webpack(config) {
            config.module.rules.push({
              test: /\.svg$/,
              use: ['@svgr/webpack'],
            });
            return config;
          },
          async rewrites() {
            return [
              {
                source: "/:path*",
                destination: `${process.env.NEXT_PUBLIC_API_SERVER_URL}/:path*`,
              },
            ];
          },
        };
        ```
        
2. Install Packages
    1. Using Yarn (Recommanded)
        ```bash 
        yarn install
        yarn dev
        ```
        
    2. Using npm
        ```bash 
        npm i   # OR npm i --legacy-peer-deps
        npm run dev
        ```