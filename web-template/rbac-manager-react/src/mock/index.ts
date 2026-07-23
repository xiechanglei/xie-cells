import {MockMethod} from 'vite-plugin-mock';
import authMocks from './mock-auth-new';
import userMocks from './mock-user';
import roleMocks from './mock-role';
import logMocks from './mock-log';

const mocks: MockMethod[] = [
    ...authMocks,
    ...userMocks,
    ...roleMocks,
    ...logMocks
];

export default mocks;