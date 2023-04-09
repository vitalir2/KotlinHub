import {configureStore} from '@reduxjs/toolkit';
import {appGraph} from "./dependency_injection";
import {repositoriesSlice} from "../features/main/MainSlice";
import {loginSlice} from "../features/login/LoginSlice";
import {repositorySlice} from "../features/repositories/repository/RepositorySlice";
import {authReducer} from "../features/auth/AuthSlice";
import logger from 'redux-logger';


export const store = configureStore({
  reducer: {
      auth: authReducer,
      repositories: repositoriesSlice.reducer,
      login: loginSlice.reducer,
      repository: repositorySlice.reducer,
  },
  middleware: getDefaultMiddleware =>
      getDefaultMiddleware({
        thunk: {
          extraArgument: {
              appGraph: appGraph,
          }
        },
      }).concat(logger)
});

export type AppDispatch = typeof store.dispatch;
export type RootState = ReturnType<typeof store.getState>;
