const path = require('path');
module.exports = {
  mode: 'development',
  entry: './src/main/resources/assets/main.js',
  output: {
    filename: 'exmp-blog.min.js',
    path: path.join(__dirname, 'src/main/webapp/docroot/scripts')
  }
};