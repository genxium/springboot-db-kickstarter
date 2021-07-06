'use-strict';

const React = require('react');
const Component = React.Component;

const WebFunc = require('../../utils/WebFunc').default;

const LocaleManager = require('../../../common/LocaleManager').default;
const NetworkFunc = require('../../../common/NetworkFunc').default;
const Time = require('../../../common/Time').default;

const Crypto = require('../../../common/Crypto').default;
const constants = require('../../../common/constants');
const signals = require('../../../common/signals');

import { View, Button, Input, Image, Text, replaceNewScene, changeSceneTitle, queryNamedGatewayInfoDictSync, } from '../../widgets/WebCommonRouteProps';

import AdminManager from "../../admin_console/AdminManager";
import WriterManager from "../../writer_console/WriterManager";
import PlayerManager from "../../player_console/PlayerManager";

class Login extends Component {

  constructor(props) {
    super(props);
    this.query = NetworkFunc.searchStrToMap(this.props.location.search);
    const roleName = this.query.role;
    this.state = {
      cachedPrincipal: "",
      cachedPassword: "",
    };
  }

  isValidRolePrincipal(str) {
    if (null == this.query) {
      return false;
    }
    const roleName = this.query.role;
    switch (roleName) {
      case constants.ROLE_NAME.ADMIN:
        return constants.REGEX.ADMIN_PRINCIPAL.test(str);
      case constants.ROLE_NAME.WRITER:
        return constants.REGEX.WRITER_PRINCIPAL.test(str);
      default:
        // Defaults to "player"
        return constants.REGEX.PLAYER_PRINCIPAL.test(str);
    }
  }

  isValidPassword(str) {
    return constants.REGEX.PASSWORD.test(str);
  }

  componentDidCatch(error, errorInfo) {
    // You can also log the error to an error reporting service
    console.log(error, errorInfo);
  }

  componentDidMount() {
    const sceneRef = this;
    changeSceneTitle(sceneRef, LocaleManager.instance.effectivePack().CENTRAL_AUTH_CONSOLE);
  }

  componentWillUnmount() {}

  triggerLoginRequest() {
    const sceneRef = this;
    const {location, basename, ...other} = sceneRef.props;

    const principal = sceneRef.state.cachedPrincipal;
    const sha1edPasswordUtf8BytesHexed = Crypto.sha1SignToHex(sceneRef.state.cachedPassword);
    const paramDict = {
      principal: principal,
      password: sha1edPasswordUtf8BytesHexed
    };

    NetworkFunc.post("/login" + location.search, paramDict)
      .then(function(response) {
        if (response.redirected) {
          throw new signals.GeneralFailure(constants.RET_CODE.REDIRECTED, response.headers.Location);
        } else {
          return response.json();
        }
      })
      .then(function(responseData) {
        if (constants.RET_CODE.OK != responseData.ret) {
          if (constants.RET_CODE.NONEXISTENT_PRINCIPAL == responseData.ret) {
            alert(LocaleManager.instance.effectivePack().NONEXISTENT_PRINCIPAL);
          }
          if (constants.RET_CODE.INCORRECT_PASSWORD == responseData.ret) {
            alert(LocaleManager.instance.effectivePack().INCORRECT_PASSWORD);
          }
          sceneRef.setState({
            cachedPassword: "",
          });
          return;
        }
      })
      .catch(function(ex) {
          if (null == ex) return;
          if (constants.RET_CODE.REDIRECTED == ex.ret) {
            replaceNewScene(sceneRef, ex.errMsg);
          } else {
            console.log(ex);
          }
      });
  }

  render() {
    const sceneRef = this;
    const {location, ...other} = sceneRef.props;

    const loginBtn = (
    <Button
            style={ {
                      fontSize: 18,
                    } }
            disabled={ !sceneRef.isValidRolePrincipal(sceneRef.state.cachedPrincipal) || !sceneRef.isValidPassword(sceneRef.state.cachedPassword) }
            onPress={ (evt) => {
                        sceneRef.triggerLoginRequest();
                      } }>
      { LocaleManager.instance.effectivePack().LOGIN }
    </Button>
    );

    const mainScene = (
    <View style={ {
                textAlign: 'center',
              } }>
      <View>
        <Input
               key='principal-input'
               style={ {
                         display: 'inline-block',
                         fontSize: 18,
                       } }
               value={ sceneRef.state.cachedPrincipal }
               onUpdated={ (evt) => {
                             sceneRef.setState({
                               cachedPrincipal: evt.target.value,
                             });
                           } }
               placeholder={ LocaleManager.instance.effectivePack().PLEASE_INPUT_PRINCIPAL } />
      </View>
      <View>
        <Input
               key='password-input'
               type='password'
               style={ {
                         display: 'inline-block',
                         fontSize: 18,
                       } }
               value={ sceneRef.state.cachedPassword }
               onUpdated={ (evt) => {
                             sceneRef.setState({
                               cachedPassword: evt.target.value,
                             });
                           } }
               placeholder={ LocaleManager.instance.effectivePack().PLEASE_INPUT_PASSWORD }
               onKeyDown={ (evt) => {
                             if (evt.keyCode != constants.KEYBOARD_CODE.RETURN) return;
                             if (!sceneRef.isValidRolePrincipal(sceneRef.state.cachedPrincipal)) return;
                             if (!sceneRef.isValidPassword(sceneRef.state.cachedPassword)) return;
                             sceneRef.triggerLoginRequest();
                           } } />
      </View>
      { loginBtn }
    </View>
    );

    return (
      <View>
        { mainScene }
      </View>
    );
  }
}

export default Login;
