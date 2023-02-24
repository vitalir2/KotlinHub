import {configureStore} from '@reduxjs/toolkit';
import {createAppGraph} from "./dependency_injection";
import {repositoriesSlice} from "../features/main/MainSlice";
import {loginSlice} from "../features/login/LoginSlice";
import {repositorySlice} from "../features/repositories/repository/RepositorySlice";
import {userSlice} from "../features/user/UserSlice";

export const store = configureStore({
  reducer: {
      user: userSlice.reducer,
      repositories: repositoriesSlice.reducer,
      login: loginSlice.reducer,
      repository: repositorySlice.reducer,
  },
  middleware: getDefaultMiddleware =>
      getDefaultMiddleware({
        thunk: {
          extraArgument: {
              appGraph: createAppGraph(),
          }
        }
      })
});

export type AppDispatch = typeof store.dispatch;
export type RootState = ReturnType<typeof store.getState>;
