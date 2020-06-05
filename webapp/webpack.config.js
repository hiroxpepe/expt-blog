const path = require('path');
module.exports = {
  mode: 'development',
  entry: './src/main/assets/scripts/main.js',
  output: {
    filename: '_exmp-blog.min.js',
    path: path.join(__dirname, 'src/main/webapp/docroot/scripts')
  }
};