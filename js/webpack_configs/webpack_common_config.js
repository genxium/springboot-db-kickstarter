const path = require('path');
const baseAbsPath = __dirname + '/';

const constants = require(baseAbsPath + '../common/constants');
const webModuleAbsPath = baseAbsPath + '../node_modules';

/*
* These entry names are used not only by the "output" directive, by also by the "SplitChunksPlugin" https://webpack.js.org/plugins/split-chunks-plugin/.
* 
* As a case study, the "shared codes/dependencies by `admin_console` & `player_console`" could be extracted and rebundled into "admin_console~player_console.bundle.js". Similarly, the "shared codes/dependencies by `admin_console` & `player_console` & `writer_console`" could be extracted and rebundled into "admin_console~player_console~writer_console.bundle.js". 
*
* Surely you want the rebundling nomenclature to be controlled and "SplitChunksPlugin" allows you to do so.   
*
* Moreover, using 
``` 
webpack.optimization.splitChunks.chunks: 'all' 
```

can drastically reduce the "total built size of all bundles", because it traverses all entries, i.e. "player_console" & "admin_console" & "writer_console" in this case, to extract and rebundle all duplicate codes/dependencies, REGARDLESS OF whether or not the shares are dynamic imports.

However, the 'all' strategy can easily go wrong, if your codes fail to execute under "webpack.optimization.splitChunks.chunks: 'all'" configuration, chances are that webpack itself has a bug, and you should disable the "SplitChunksPlugin" temporarily to better seek the root cause. 
*/
let entryObj = null;
let outputPath = null;
switch (process.env.TARGET) {
case "exam":
  entryObj = {
		writer_console: baseAbsPath + '../consoles/writer_console/index.js',
		player_console: baseAbsPath + '../consoles/player_console/index.js',
		admin_console: baseAbsPath + '../consoles/admin_console/index.js',
    
    // Specifically added to avoid "code splitting dilema" described in '../player_console/index.js'.
		player_landing: baseAbsPath + '../consoles/player_console/landing.js'
  };
break;
case "auth":
  entryObj = {
		central_auth_console: baseAbsPath + '../consoles/central_auth_console/index.js'
  };
break;
}

if (null == entryObj) {
  console.error("No target to build, please specify `process.env.TARGET`.");
  return;
}

outputPath = baseAbsPath + '../bin/' + process.env.TARGET;

const commonConfig = {
	resolve: {
		modules: [
			webModuleAbsPath
		]
	},
	resolveLoader: {
		modules: [
      webModuleAbsPath // This helps to resolve loader names, e.g. 'babel-loader'
    ]
	},
	entry: entryObj,
	module: {
		rules: [
			{
				test: /\.jsx?$/,
				exclude: /(node_modules|bower_components)/,
        use: {
          loader: 'babel-loader',
          options: {
            presets: ['@babel/preset-env', '@babel/preset-react']
          }
        },
			},
			{
				test: /\.(css)$/,
				exclude: /\.useable\.css$/,
				use: ['style-loader', 'css-loader']
			},
      {
        test: /\.(jpg|jpeg|png|woff|woff2|eot|ttf|svg)$/,
        loader: 'url-loader?limit=100000'
      }
		]
	},
	output: {
		path: outputPath,
		publicPath: constants.ROUTE_PATHS.BASE + '/bin/', // NOTE: For chunk loading.
		filename: '[name].bundle.js',
		chunkFilename: '[name].bundle.js',
		sourceMapFilename: '[file].map'
	}
};

exports.default = commonConfig; 
