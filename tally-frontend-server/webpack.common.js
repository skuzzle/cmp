const MiniCssExtractPlugin = require('mini-css-extract-plugin');

const devMode = process.env.NODE_ENV !== 'production';
const path = require('path');

module.exports = {
    entry: './src/main/js/index.js',
    output: {
        filename: 'main.js',
        path: path.resolve(__dirname, 'target/classes/static'),
    },

    plugins: [new MiniCssExtractPlugin({
            // Options similar to the same options in webpackOptions.output
            // both options are optional
            filename: devMode ? '[name].css' : '[name].[hash].css',
            chunkFilename: devMode ? '[id].css' : '[id].[hash].css',
        }), ],
    module: {
        rules: [{
                test: /\.(sa|sc|c)ss$/,
                use: [{
                        loader: MiniCssExtractPlugin.loader,
                        options: {
                            hmr: process.env.NODE_ENV === 'development',
                        },
                    }, 'css-loader', 'sass-loader', ],
            }, ],
    },
};
